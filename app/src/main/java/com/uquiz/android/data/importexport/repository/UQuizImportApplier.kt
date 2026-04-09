package com.uquiz.android.data.importexport.repository

import com.uquiz.android.data.importexport.dto.ImportFolderNode
import com.uquiz.android.data.importexport.dto.ImportPackNode
import com.uquiz.android.domain.content.repository.CreateOptionData
import com.uquiz.android.domain.content.repository.FolderRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.content.repository.QuestionRepository
import com.uquiz.android.domain.importexport.projection.ImportResult

/**
 * Aplica recursivamente el árbol importado desde un archivo `.uquiz` creando las
 * entidades correspondientes en el repositorio dentro de una transacción ya abierta.
 *
 * El llamador es responsable de abrir y gestionar la transacción de base de datos.
 */
internal class UQuizImportApplier(
    private val folderRepository: FolderRepository,
    private val packRepository: PackRepository,
    private val questionRepository: QuestionRepository,
) {

    /**
     * Aplica el subárbol de carpeta raíz y devuelve el resumen de la operación.
     *
     * @param root Nodo raíz de carpeta a importar.
     * @param parentId Identificador de la carpeta padre, o `null` para la raíz de la biblioteca.
     */
    suspend fun applyFolderSubtree(root: ImportFolderNode, parentId: String?): ImportResult {
        val counts = Counts()
        val created = applyFolder(root, parentId, counts)
        return counts.toResult(rootName = created.name)
    }

    /**
     * Aplica un pack raíz dentro de la carpeta indicada y devuelve el resumen de la operación.
     *
     * @param root Nodo pack a importar.
     * @param folderId Identificador de la carpeta destino.
     */
    suspend fun applyPackSubtree(root: ImportPackNode, folderId: String): ImportResult {
        val counts = Counts()
        val created = applyPack(root, folderId, counts)
        return counts.toResult(rootName = created.title)
    }

    private suspend fun applyFolder(
        node: ImportFolderNode,
        parentId: String?,
        counts: Counts,
    ): com.uquiz.android.domain.content.model.Folder {
        val folder = folderRepository.createFolder(
            name = node.name,
            parentId = parentId,
            colorHex = node.colorHex,
            icon = node.icon,
        )
        counts.folders += 1
        node.folders.forEach { child -> applyFolder(child, folder.id, counts) }
        node.packs.forEach { pack -> applyPack(pack, folder.id, counts) }
        return folder
    }

    private suspend fun applyPack(
        node: ImportPackNode,
        folderId: String,
        counts: Counts,
    ): com.uquiz.android.domain.content.model.Pack {
        val pack = packRepository.createPack(
            title = node.title,
            description = node.description,
            folderId = folderId,
            icon = node.icon,
            colorHex = node.colorHex,
        )
        counts.packs += 1
        node.questions.forEachIndexed { index, question ->
            val created = questionRepository.createQuestion(
                text = question.text,
                explanation = question.explanation,
                difficulty = question.difficulty,
                options = question.options
                    .sortedBy { it.position }
                    .map { CreateOptionData(label = it.label, text = it.text, isCorrect = it.isCorrect) },
            )
            packRepository.addQuestionToPack(pack.id, created.id, index)
            counts.questions += 1
        }
        return pack
    }

    private data class Counts(
        var folders: Int = 0,
        var packs: Int = 0,
        var questions: Int = 0,
    ) {
        fun toResult(rootName: String) = ImportResult(
            rootName = rootName,
            createdFolderCount = folders,
            createdPackCount = packs,
            createdQuestionCount = questions,
        )
    }
}
