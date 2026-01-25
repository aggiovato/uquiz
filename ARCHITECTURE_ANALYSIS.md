# UQuiz - Análisis de Arquitectura y Propuesta de Diseño

## 📋 Tabla de Contenidos
1. [Resumen Ejecutivo](#resumen-ejecutivo)
2. [Estructura Actual del Proyecto](#estructura-actual-del-proyecto)
3. [Análisis de Requisitos](#análisis-de-requisitos)
4. [Diseño Propuesto](#diseño-propuesto)
5. [Diagrama Entidad-Relación](#diagrama-entidad-relación)
6. [Estrategia de Multiidioma](#estrategia-de-multiidioma)
7. [Operaciones Principales y Cascadas](#operaciones-principales-y-cascadas)
8. [Cambios Necesarios](#cambios-necesarios)
9. [Plan de Implementación](#plan-de-implementación)

---

## 🎯 Resumen Ejecutivo

**Estado Actual**: Proyecto en fase inicial con base de datos Room configurada, arquitectura limpia definida, pero sin implementación de UI, ViewModels, repositorios concretos, ni soporte para folders anidables o multiidioma.

**Objetivo**: Aplicación Android de quiz/test con:
- ✅ Preguntas y opciones con soporte multi-respuesta
- ✅ Packs para agrupar preguntas
- 🆕 **Folders anidables infinitamente** (por implementar)
- 🆕 **Multiidioma** (EN, ES, CA) con fallback a EN
- 🆕 Dos modos: Study (orden fijo) y Game (orden aleatorio)

---

## 📁 Estructura Actual del Proyecto

### Arquitectura de Capas

```
com.uquiz.android/
├── core/di/                    # ❌ VACÍO - DI no configurado
├── data/
│   ├── local/
│   │   ├── dao/                # ✅ QuestionDao, PackDao
│   │   ├── db/                 # ✅ UQuizDatabase, Converters
│   │   ├── entity/             # ✅ 4 entidades definidas
│   │   ├── mapper/             # ❌ VACÍO - Sin mappers
│   │   └── relation/           # ✅ OrderedQuestionWithOptions
│   └── repository/             # ❌ VACÍO - Sin implementaciones
├── domain/
│   ├── model/                  # ✅ Question, Pack, Option
│   └── repository/             # ✅ Interfaces definidas
└── ui/
    ├── navigation/             # ❌ VACÍO - Sin navegación
    └── theme/                  # ✅ Material3 theme configurado
```

### Entidades Existentes

#### 1. QuestionEntity
```kotlin
@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: String,
    val text: String,
    val correctOptionIds: List<String>  // JSON via TypeConverter
)
```

**Campos actuales**: 3
**Problemas**:
- ❌ Sin multiidioma
- ❌ Sin campo `difficulty`
- ❌ Sin campo `explanation`
- ❌ Sin relación obligatoria con Pack
- ❌ Sin timestamps

#### 2. OptionEntity
```kotlin
@Entity(
    tableName = "options",
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE  // ✅ CASCADE OK
        )
    ],
    indices = [Index("questionId")]
)
data class OptionEntity(
    @PrimaryKey val id: String,
    val questionId: String,
    val text: String
)
```

**Campos actuales**: 3
**Problemas**:
- ❌ Sin campo `position` para ordenar en modo Study
- ❌ Sin multiidioma

#### 3. PackEntity
```kotlin
@Entity(tableName = "packs")
data class PackEntity(
    @PrimaryKey val id: String,
    val name: String
)
```

**Campos actuales**: 2
**Problemas**:
- ❌ Sin campo `description`
- ❌ Sin campo `folderId` (relación con Folder)
- ❌ Sin multiidioma
- ❌ Sin timestamps

#### 4. PackQuestionEntity
```kotlin
@Entity(
    tableName = "pack_questions",
    primaryKeys = ["packId", "questionId"],
    indices = [Index("packId"), Index("questionId")]
)
data class PackQuestionEntity(
    val packId: String,
    val questionId: String,
    val position: Int
)
```

**Campos actuales**: 3
**Problemas Críticos**:
- 🚨 **Sin Foreign Keys**: No hay constraints de integridad
- 🚨 **Riesgo de registros huérfanos** al eliminar Packs o Questions
- ❌ No hay cascada de eliminación configurada

---

## 📝 Análisis de Requisitos

### Jerarquía de Contenidos Definida

```
Folder (anidable infinitamente)
  ├── Folder (hijo)
  │     ├── Pack
  │     │     └── Question
  │     │           └── Option
  │     └── Pack
  └── Pack
        └── Question
              └── Option
```

### Reglas de Negocio

1. **Question**:
   - ✅ Siempre debe pertenecer a un Pack (relación obligatoria)
   - ✅ Tiene texto, opciones, explicación, dificultad
   - ✅ Modo Study: opciones en orden predefinido
   - ✅ Modo Game: opciones en orden aleatorio
   - ✅ Multiidioma: EN, ES, CA (fallback a EN)

2. **Option**:
   - ✅ Pertenece a una Question
   - ✅ Tiene orden (`position`) para modo Study
   - ✅ Multiidioma en el texto

3. **Pack**:
   - ✅ Siempre debe estar dentro de un Folder (relación obligatoria)
   - ✅ Contiene Questions (no puede contener Folders)
   - ✅ Tiene nombre y descripción
   - ✅ Multiidioma
   - ✅ Al eliminar Pack → eliminar todas las Questions dentro

4. **Folder**:
   - ✅ Puede contener: Packs y otros Folders
   - ✅ Anidamiento infinito mediante `parentId` (self-reference)
   - ✅ No puede contener Questions directamente
   - ✅ Multiidioma
   - ✅ Al eliminar Folder → eliminar todos los Folders hijos y Packs dentro (recursivo)

### Cascadas de Eliminación

```
DELETE Folder
  ├─→ CASCADE DELETE todos los Folders hijos (recursivo)
  ├─→ CASCADE DELETE todos los Packs dentro
  │     └─→ CASCADE DELETE todas las Questions de esos Packs
  │           └─→ CASCADE DELETE todas las Options de esas Questions
  └─→ CASCADE DELETE PackQuestionEntity entries
```

```
DELETE Pack
  ├─→ CASCADE DELETE todas las Questions asociadas
  │     └─→ CASCADE DELETE todas las Options de esas Questions
  └─→ CASCADE DELETE PackQuestionEntity entries
```

```
DELETE Question
  ├─→ CASCADE DELETE todas las Options
  └─→ CASCADE DELETE PackQuestionEntity entries
```

---

## 🏗️ Diseño Propuesto

### Nueva Estructura de Entidades

#### 1. FolderEntity (NUEVA)

```kotlin
@Entity(
    tableName = "folders",
    foreignKeys = [
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.CASCADE  // Eliminar hijos al eliminar padre
        )
    ],
    indices = [Index("parentId")]
)
data class FolderEntity(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameEs: String,
    val nameCa: String,
    val parentId: String? = null,  // null = carpeta raíz
    val position: Int = 0,          // Orden dentro del folder padre
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
```

**Características**:
- ✅ Self-reference para anidamiento infinito
- ✅ CASCADE DELETE para eliminar jerarquía completa
- ✅ Multiidioma directo en campos
- ✅ Soporta ordenamiento con `position`

#### 2. PackEntity (MODIFICADA)

```kotlin
@Entity(
    tableName = "packs",
    foreignKeys = [
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["folderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("folderId")]
)
data class PackEntity(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameEs: String,
    val nameCa: String,
    val descriptionEn: String? = null,
    val descriptionEs: String? = null,
    val descriptionCa: String? = null,
    val folderId: String,  // ⚠️ NOT NULL - Pack siempre en un Folder
    val position: Int = 0,  // Orden dentro del folder
    val color: String? = null,  // Color para UI (#RRGGBB)
    val icon: String? = null,   // Nombre de ícono
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
```

**Cambios**:
- ✅ Añadido `folderId` NOT NULL con FK CASCADE
- ✅ Añadido `description` multiidioma
- ✅ Multiidioma en `name`
- ✅ Añadidos `color` e `icon` para personalización
- ✅ Timestamps para auditoría

#### 3. QuestionEntity (MODIFICADA)

```kotlin
@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: String,
    val textEn: String,
    val textEs: String,
    val textCa: String,
    val explanationEn: String? = null,
    val explanationEs: String? = null,
    val explanationCa: String? = null,
    val correctOptionIds: List<String>,  // JSON via TypeConverter
    val difficulty: DifficultyLevel = DifficultyLevel.MEDIUM,
    val tags: List<String> = emptyList(),  // JSON via TypeConverter
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class DifficultyLevel {
    EASY,
    MEDIUM,
    HARD,
    EXPERT
}
```

**Cambios**:
- ✅ Multiidioma en `text` y `explanation`
- ✅ Añadido `difficulty` con enum
- ✅ Añadido `tags` para categorización
- ✅ Timestamps

**Nota**: La relación obligatoria con Pack se gestiona mediante `PackQuestionEntity`, no con FK directo. Esto permite que una pregunta pueda reutilizarse en múltiples packs si fuera necesario en el futuro.

#### 4. OptionEntity (MODIFICADA)

```kotlin
@Entity(
    tableName = "options",
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("questionId")]
)
data class OptionEntity(
    @PrimaryKey val id: String,
    val questionId: String,
    val textEn: String,
    val textEs: String,
    val textCa: String,
    val position: Int,  // 🆕 Para modo Study (orden fijo)
    val createdAt: Long = System.currentTimeMillis()
)
```

**Cambios**:
- ✅ Multiidioma en `text`
- ✅ Añadido `position` para modo Study
- ✅ Timestamp

#### 5. PackQuestionEntity (MODIFICADA CON FKs)

```kotlin
@Entity(
    tableName = "pack_questions",
    primaryKeys = ["packId", "questionId"],
    foreignKeys = [
        ForeignKey(
            entity = PackEntity::class,
            parentColumns = ["id"],
            childColumns = ["packId"],
            onDelete = ForeignKey.CASCADE  // 🆕 Eliminar al borrar Pack
        ),
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE  // 🆕 Eliminar al borrar Question
        )
    ],
    indices = [Index("packId"), Index("questionId")]
)
data class PackQuestionEntity(
    val packId: String,
    val questionId: String,
    val position: Int  // Orden de la pregunta dentro del Pack
)
```

**Cambios Críticos**:
- ✅ Añadidos Foreign Keys a Pack y Question
- ✅ CASCADE DELETE configurado en ambos
- ✅ Garantiza integridad referencial

---

## 🗂️ Diagrama Entidad-Relación

### Diagrama Completo

```
┌──────────────────────────────────────────────────────────────────────┐
│                        UQUIZ DATABASE SCHEMA                          │
└──────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                          FolderEntity                            │
├──────────────────────────────────────────────────────────────────┤
│ PK  id: String                                                   │
│     nameEn: String                                               │
│     nameEs: String                                               │
│     nameCa: String                                               │
│ FK  parentId: String? ───────┐                                   │
│     position: Int            │  (self-reference)                 │
│     createdAt: Long          │  CASCADE DELETE                   │
│     updatedAt: Long          │                                   │
└─────────────────────────────┼────────────────────────────────────┘
                              │
                              └─── (anidamiento infinito)

        │ 1:N
        │
        ▼
┌──────────────────────────────────────────────────────────────────┐
│                           PackEntity                              │
├───────────────────────────────────────────────────────────────────┤
│ PK  id: String                                                    │
│     nameEn: String                                                │
│     nameEs: String                                                │
│     nameCa: String                                                │
│     descriptionEn: String?                                        │
│     descriptionEs: String?                                        │
│     descriptionCa: String?                                        │
│ FK  folderId: String NOT NULL  ← CASCADE DELETE                   │
│     position: Int                                                 │
│     color: String?                                                │
│     icon: String?                                                 │
│     createdAt: Long                                               │
│     updatedAt: Long                                               │
└───────────────────────────────────────────────────────────────────┘
        │
        │ M:N (via PackQuestionEntity)
        │
        ▼
┌───────────────────────────────────────────────────────────────────┐
│                      PackQuestionEntity                            │
│                     (Junction Table)                               │
├────────────────────────────────────────────────────────────────────┤
│ PK  packId: String       ← FK CASCADE DELETE                       │
│ PK  questionId: String   ← FK CASCADE DELETE                       │
│     position: Int                                                  │
└────────────────────────────────────────────────────────────────────┘
        │
        │ N:1
        │
        ▼
┌────────────────────────────────────────────────────────────────────┐
│                        QuestionEntity                               │
├─────────────────────────────────────────────────────────────────────┤
│ PK  id: String                                                      │
│     textEn: String                                                  │
│     textEs: String                                                  │
│     textCa: String                                                  │
│     explanationEn: String?                                          │
│     explanationEs: String?                                          │
│     explanationCa: String?                                          │
│     correctOptionIds: List<String>  (JSON)                          │
│     difficulty: DifficultyLevel                                     │
│     tags: List<String>  (JSON)                                      │
│     createdAt: Long                                                 │
│     updatedAt: Long                                                 │
└─────────────────────────────────────────────────────────────────────┘
        │
        │ 1:N
        │
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         OptionEntity                                 │
├──────────────────────────────────────────────────────────────────────┤
│ PK  id: String                                                       │
│ FK  questionId: String  ← CASCADE DELETE                             │
│     textEn: String                                                   │
│     textEs: String                                                   │
│     textCa: String                                                   │
│     position: Int       (para modo Study)                            │
│     createdAt: Long                                                  │
└──────────────────────────────────────────────────────────────────────┘
```

### Relaciones y Cardinalidad

| Relación | Tipo | Descripción |
|----------|------|-------------|
| **Folder → Folder** | 1:N (self) | Un folder puede contener múltiples folders hijos |
| **Folder → Pack** | 1:N | Un folder puede contener múltiples packs |
| **Pack → Question** | M:N | Un pack puede tener múltiples questions, y una question puede estar en múltiples packs (via PackQuestionEntity) |
| **Question → Option** | 1:N | Una question tiene múltiples options |

---

## 🌍 Estrategia de Multiidioma

### Opción Seleccionada: Campos Directos en Entidades

**Ventajas**:
- ✅ Queries más simples y rápidas (sin JOINs adicionales)
- ✅ Menos complejidad en DAOs
- ✅ Type-safe desde la base de datos
- ✅ Mejor rendimiento (sin lookups adicionales)
- ✅ Room genera código optimizado

**Desventajas**:
- ⚠️ Agregar idiomas requiere migración de BD
- ⚠️ Más columnas en las tablas

### Modelo de Datos Multiidioma

#### En Entidades (Room)
```kotlin
data class PackEntity(
    val nameEn: String,
    val nameEs: String,
    val nameCa: String
)
```

#### En Modelos de Dominio
```kotlin
data class Pack(
    val id: String,
    val name: LocalizedString,
    val description: LocalizedString?
)

data class LocalizedString(
    val en: String,
    val es: String,
    val ca: String
) {
    fun get(locale: String): String = when (locale) {
        "es" -> es.ifEmpty { en }
        "ca" -> ca.ifEmpty { en }
        else -> en
    }

    fun getOrFallback(locale: String): String = when (locale) {
        "es" -> es.ifEmpty { en }
        "ca" -> ca.ifEmpty { es.ifEmpty { en } }
        else -> en
    }
}
```

### Mappers Entidad → Dominio

```kotlin
// PackEntityMapper.kt
object PackEntityMapper {
    fun toDomain(entity: PackEntity): Pack = Pack(
        id = entity.id,
        name = LocalizedString(
            en = entity.nameEn,
            es = entity.nameEs,
            ca = entity.nameCa
        ),
        description = if (entity.descriptionEn != null) {
            LocalizedString(
                en = entity.descriptionEn,
                es = entity.descriptionEs ?: "",
                ca = entity.descriptionCa ?: ""
            )
        } else null,
        folderId = entity.folderId,
        position = entity.position,
        color = entity.color,
        icon = entity.icon,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toEntity(domain: Pack): PackEntity = PackEntity(
        id = domain.id,
        nameEn = domain.name.en,
        nameEs = domain.name.es,
        nameCa = domain.name.ca,
        descriptionEn = domain.description?.en,
        descriptionEs = domain.description?.es,
        descriptionCa = domain.description?.ca,
        folderId = domain.folderId,
        position = domain.position,
        color = domain.color,
        icon = domain.icon,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt
    )
}
```

### Gestión de Locale en la App

```kotlin
// LocaleManager.kt
object LocaleManager {
    private val supportedLocales = setOf("en", "es", "ca")

    fun getCurrentLocale(context: Context): String {
        val locale = Locale.getDefault().language
        return if (locale in supportedLocales) locale else "en"
    }

    fun setLocale(context: Context, locale: String) {
        if (locale !in supportedLocales) return

        val config = context.resources.configuration
        val newLocale = Locale(locale)
        Locale.setDefault(newLocale)
        config.setLocale(newLocale)

        context.createConfigurationContext(config)
    }
}
```

### Uso en Composables

```kotlin
@Composable
fun PackItem(pack: Pack) {
    val locale = Locale.getDefault().language

    Text(
        text = pack.name.get(locale),
        style = MaterialTheme.typography.titleMedium
    )

    pack.description?.let { desc ->
        Text(
            text = desc.get(locale),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
```

---

## 🔄 Operaciones Principales y Cascadas

### 1. Crear Jerarquía Completa

```kotlin
// Caso de uso: Usuario crea una nueva estructura
suspend fun createQuizStructure() {
    // 1. Crear Folder raíz
    val rootFolder = FolderEntity(
        id = UUID.randomUUID().toString(),
        nameEn = "Mathematics",
        nameEs = "Matemáticas",
        nameCa = "Matemàtiques",
        parentId = null
    )
    folderDao.upsert(rootFolder)

    // 2. Crear Folder hijo
    val subFolder = FolderEntity(
        id = UUID.randomUUID().toString(),
        nameEn = "Algebra",
        nameEs = "Álgebra",
        nameCa = "Àlgebra",
        parentId = rootFolder.id,
        position = 0
    )
    folderDao.upsert(subFolder)

    // 3. Crear Pack dentro del subfolder
    val pack = PackEntity(
        id = UUID.randomUUID().toString(),
        nameEn = "Linear Equations",
        nameEs = "Ecuaciones Lineales",
        nameCa = "Equacions Lineals",
        descriptionEn = "Basic linear equations",
        folderId = subFolder.id
    )
    packDao.upsert(pack)

    // 4. Crear Question
    val question = QuestionEntity(
        id = UUID.randomUUID().toString(),
        textEn = "Solve: 2x + 3 = 7",
        textEs = "Resuelve: 2x + 3 = 7",
        textCa = "Resol: 2x + 3 = 7",
        explanationEn = "Subtract 3 from both sides, then divide by 2",
        difficulty = DifficultyLevel.EASY,
        correctOptionIds = listOf("opt1")
    )
    questionDao.upsert(question)

    // 5. Crear Options
    val options = listOf(
        OptionEntity(
            id = "opt1",
            questionId = question.id,
            textEn = "x = 2",
            textEs = "x = 2",
            textCa = "x = 2",
            position = 0
        ),
        OptionEntity(
            id = "opt2",
            questionId = question.id,
            textEn = "x = 5",
            textEs = "x = 5",
            textCa = "x = 5",
            position = 1
        )
    )
    questionDao.upsertOptions(options)

    // 6. Vincular Question a Pack
    val packQuestion = PackQuestionEntity(
        packId = pack.id,
        questionId = question.id,
        position = 0
    )
    packDao.upsertPackQuestions(listOf(packQuestion))
}
```

### 2. Eliminar Folder (Cascada Completa)

```kotlin
// Al ejecutar esto:
folderDao.delete(rootFolderId)

// Room automáticamente ejecuta:
// 1. DELETE FROM folders WHERE parentId = rootFolderId (folders hijos)
// 2. Para cada folder hijo, repite el paso 1 (recursivo por CASCADE)
// 3. DELETE FROM packs WHERE folderId = rootFolderId (packs directos)
// 4. DELETE FROM packs WHERE folderId IN (folders_hijos_ids) (packs de hijos)
// 5. DELETE FROM pack_questions WHERE packId IN (packs_eliminados)
// 6. DELETE FROM questions WHERE id IN (questions_de_packs_eliminados)
// 7. DELETE FROM options WHERE questionId IN (questions_eliminadas)
```

**Resultado**: Se eliminan todos los folders hijos, packs, questions y options en cascada.

### 3. Eliminar Pack

```kotlin
packDao.delete(packId)

// Cascada automática:
// 1. DELETE FROM pack_questions WHERE packId = packId
// 2. DELETE FROM questions WHERE id IN (SELECT questionId FROM pack_questions WHERE packId = packId)
// 3. DELETE FROM options WHERE questionId IN (questions_eliminadas)
```

### 4. Eliminar Question

```kotlin
questionDao.delete(questionId)

// Cascada automática:
// 1. DELETE FROM options WHERE questionId = questionId
// 2. DELETE FROM pack_questions WHERE questionId = questionId
```

### 5. Mover Pack entre Folders

```kotlin
suspend fun movePack(packId: String, newFolderId: String) {
    val pack = packDao.getPack(packId)
    packDao.update(pack.copy(folderId = newFolderId))
}
```

### 6. Mover Folder (con toda su jerarquía)

```kotlin
suspend fun moveFolder(folderId: String, newParentId: String?) {
    // Validar que no se cree ciclo (folder no puede ser su propio ancestro)
    if (isAncestor(newParentId, folderId)) {
        throw IllegalArgumentException("Cannot move folder to its own descendant")
    }

    val folder = folderDao.getFolder(folderId)
    folderDao.update(folder.copy(parentId = newParentId))
    // Toda la jerarquía se mueve automáticamente porque usan folderId
}

private suspend fun isAncestor(ancestorId: String?, descendantId: String): Boolean {
    var currentId = descendantId
    while (currentId != null) {
        if (currentId == ancestorId) return true
        val folder = folderDao.getFolder(currentId)
        currentId = folder?.parentId
    }
    return false
}
```

### 7. Obtener Jerarquía Completa de Folders

```kotlin
@Query("""
    WITH RECURSIVE folder_tree AS (
        -- Caso base: folder raíz o el folder solicitado
        SELECT * FROM folders WHERE id = :rootFolderId

        UNION ALL

        -- Caso recursivo: obtener folders hijos
        SELECT f.*
        FROM folders f
        INNER JOIN folder_tree ft ON f.parentId = ft.id
    )
    SELECT * FROM folder_tree ORDER BY position
""")
fun getFolderHierarchy(rootFolderId: String): Flow<List<FolderEntity>>
```

### 8. Obtener Contenido de un Folder

```kotlin
// En FolderDao
@Transaction
@Query("""
    SELECT * FROM folders
    WHERE parentId = :folderId
    ORDER BY position
""")
fun getChildFolders(folderId: String): Flow<List<FolderEntity>>

@Transaction
@Query("""
    SELECT * FROM packs
    WHERE folderId = :folderId
    ORDER BY position
""")
fun getPacksInFolder(folderId: String): Flow<List<PackEntity>>

// Combinado en ViewModel
data class FolderContent(
    val subfolders: List<Folder>,
    val packs: List<Pack>
)

fun getFolderContent(folderId: String): Flow<FolderContent> =
    combine(
        folderRepository.getChildFolders(folderId),
        packRepository.getPacksInFolder(folderId)
    ) { folders, packs ->
        FolderContent(folders, packs)
    }
```

### 9. Modo Study vs Game (Opciones)

```kotlin
// Modo Study: orden según position
@Query("""
    SELECT * FROM options
    WHERE questionId = :questionId
    ORDER BY position ASC
""")
fun getOptionsForStudyMode(questionId: String): Flow<List<OptionEntity>>

// Modo Game: orden aleatorio en ViewModel
fun getOptionsForGameMode(questionId: String): Flow<List<Option>> =
    questionRepository.getOptions(questionId)
        .map { options -> options.shuffled() }
```

---

## 🔧 Cambios Necesarios

### Resumen de Cambios por Componente

| Componente | Estado Actual | Cambios Necesarios |
|------------|---------------|-------------------|
| **FolderEntity** | ❌ No existe | ✅ Crear nueva entidad con self-reference |
| **PackEntity** | ⚠️ Incompleta | ✅ Agregar folderId, description, multiidioma, campos UI |
| **QuestionEntity** | ⚠️ Incompleta | ✅ Agregar multiidioma, difficulty, explanation, tags, timestamps |
| **OptionEntity** | ⚠️ Incompleta | ✅ Agregar position, multiidioma, timestamp |
| **PackQuestionEntity** | 🚨 Sin FKs | ✅ Agregar Foreign Keys con CASCADE |
| **UQuizDatabase** | ⚠️ Versión 1 | ✅ Actualizar a versión 2 con migración |
| **Converters** | ✅ Solo List<String> | ✅ Agregar converter para DifficultyLevel |
| **FolderDao** | ❌ No existe | ✅ Crear DAO completo |
| **PackDao** | ⚠️ Básico | ✅ Agregar queries de folder, movimiento |
| **QuestionDao** | ⚠️ Básico | ✅ Agregar queries de difficulty, tags |
| **Mappers** | ❌ Vacío | ✅ Crear todos los mappers (Folder, Pack, Question, Option) |
| **Repositories** | ❌ Solo interfaces | ✅ Implementar todas las interfaces |
| **Domain Models** | ⚠️ Básicos | ✅ Agregar LocalizedString, Folder, campos faltantes |
| **ViewModels** | ❌ No existen | ✅ Crear VMs para Folder, Pack, Quiz, Editor |
| **UI Composables** | ❌ Solo Greeting | ✅ Crear todas las pantallas |
| **Navigation** | ❌ Vacío | ✅ Configurar NavGraph |
| **DI (Hilt)** | ❌ Vacío | ✅ Configurar módulos |
| **Strings.xml** | ⚠️ Solo app_name | ✅ Agregar strings UI (en/es/ca) |

### Cambios Críticos (Orden de Implementación)

#### Fase 1: Base de Datos (PRIORITARIO)

1. **Crear `DifficultyLevel` enum**
   ```kotlin
   enum class DifficultyLevel {
       EASY, MEDIUM, HARD, EXPERT
   }
   ```

2. **Actualizar `Converters.kt`**
   - Agregar converter para `DifficultyLevel`

3. **Modificar todas las entidades** según diseño propuesto
   - FolderEntity (nueva)
   - PackEntity (agregar folderId NOT NULL, multiidioma, description)
   - QuestionEntity (agregar multiidioma, difficulty, explanation, tags)
   - OptionEntity (agregar position, multiidioma)
   - PackQuestionEntity (agregar Foreign Keys)

4. **Crear migración de BD**
   ```kotlin
   val MIGRATION_1_2 = object : Migration(1, 2) {
       override fun migrate(database: SupportSQLiteDatabase) {
           // Crear tabla folders
           // Modificar tabla packs (agregar columnas)
           // Modificar tabla questions (agregar columnas)
           // Modificar tabla options (agregar columnas)
           // Agregar FKs a pack_questions
       }
   }
   ```

5. **Actualizar `UQuizDatabase`**
   - Versión 2
   - Agregar FolderEntity
   - Agregar migración

6. **Crear `FolderDao`** con queries básicas y recursivas

#### Fase 2: Capa de Dominio

7. **Crear `LocalizedString` data class**

8. **Crear `Folder` domain model**

9. **Actualizar modelos de dominio existentes**
   - Pack (agregar description, folderId, LocalizedString)
   - Question (agregar explanation, difficulty, tags, LocalizedString)
   - Option (agregar position, LocalizedString)

10. **Crear `FolderRepository` interface**

11. **Actualizar interfaces de repositorios**
    - Agregar métodos para jerarquía, movimiento, etc.

#### Fase 3: Implementaciones

12. **Crear Mappers** en `/data/local/mapper/`
    - FolderEntityMapper
    - PackEntityMapper
    - QuestionEntityMapper
    - OptionEntityMapper

13. **Implementar Repositorios** en `/data/repository/`
    - FolderRepositoryImpl
    - PackRepositoryImpl
    - QuestionRepositoryImpl

#### Fase 4: Dependency Injection

14. **Configurar Hilt**
    - Agregar dependencia en build.gradle
    - Crear Application class con @HiltAndroidApp
    - DatabaseModule
    - RepositoryModule

#### Fase 5: Presentación

15. **Crear ViewModels**
    - FolderListViewModel
    - PackDetailViewModel
    - QuizViewModel
    - QuestionEditorViewModel

16. **Crear UI Composables**
    - FolderListScreen
    - PackDetailScreen
    - QuizScreen
    - ResultScreen
    - QuestionEditorScreen

17. **Configurar Navigation**

18. **Internacionalización**
    - Crear res/values-es/strings.xml
    - Crear res/values-ca/strings.xml
    - Migrar strings hardcodeados

---

## 📋 Plan de Implementación

### Sprint 1: Base de Datos (3-5 días)
- [ ] Crear enums y TypeConverters
- [ ] Diseñar e implementar FolderEntity
- [ ] Actualizar todas las entidades con multiidioma
- [ ] Agregar Foreign Keys a PackQuestionEntity
- [ ] Crear migración 1→2
- [ ] Crear FolderDao
- [ ] Actualizar DAOs existentes
- [ ] Tests unitarios de DAOs

### Sprint 2: Dominio y Mappers (2-3 días)
- [ ] Crear LocalizedString
- [ ] Crear/actualizar modelos de dominio
- [ ] Crear FolderRepository interface
- [ ] Actualizar interfaces de repositorios
- [ ] Implementar todos los Mappers
- [ ] Tests unitarios de Mappers

### Sprint 3: Repositorios (2-3 días)
- [ ] Implementar FolderRepositoryImpl
- [ ] Implementar PackRepositoryImpl
- [ ] Implementar QuestionRepositoryImpl
- [ ] Tests de integración de repositorios

### Sprint 4: Dependency Injection (1-2 días)
- [ ] Configurar Hilt
- [ ] Crear módulos de DI
- [ ] Verificar grafo de dependencias

### Sprint 5: ViewModels (2-3 días)
- [ ] FolderListViewModel
- [ ] PackDetailViewModel
- [ ] QuizViewModel
- [ ] QuestionEditorViewModel
- [ ] Tests de ViewModels

### Sprint 6: UI - Navegación y Estructura (3-4 días)
- [ ] Configurar Navigation Compose
- [ ] FolderListScreen (navegación de carpetas)
- [ ] PackDetailScreen (lista de preguntas)
- [ ] Integrar ViewModels con UI

### Sprint 7: UI - Quiz y Edición (3-4 días)
- [ ] QuizScreen (modo Study y Game)
- [ ] ResultScreen
- [ ] QuestionEditorScreen
- [ ] Validaciones de formularios

### Sprint 8: Internacionalización (2 días)
- [ ] Crear strings.xml para es/ca
- [ ] Implementar LocaleManager
- [ ] Screen de configuración de idioma
- [ ] Verificar traducciones completas

### Sprint 9: Testing y Polish (3-4 días)
- [ ] Tests E2E de flujos principales
- [ ] UI tests con Compose
- [ ] Corregir bugs
- [ ] Optimizaciones de rendimiento
- [ ] Documentación

**Tiempo Estimado Total**: 21-32 días (4-6 semanas)

---

## 📊 Métricas de Complejidad

### Base de Datos
- **Entidades**: 5 (4 existentes + 1 nueva)
- **DAOs**: 3 (2 existentes + 1 nuevo)
- **Relaciones**: 5 (1:N Folder→Folder, 1:N Folder→Pack, M:N Pack→Question, 1:N Question→Option, 1:N en PackQuestion)
- **Índices**: ~10
- **Migraciones**: 1 (versión 1→2)

### Código
- **Líneas de código estimadas**:
  - Entidades y DAOs: ~800 líneas
  - Mappers: ~400 líneas
  - Repositorios: ~600 líneas
  - ViewModels: ~800 líneas
  - UI Composables: ~1500 líneas
  - Tests: ~1000 líneas
- **Total estimado**: ~5100 líneas de código

### Pantallas UI
- FolderListScreen (navegación jerárquica)
- PackDetailScreen (lista de preguntas)
- QuizScreen (modo Study/Game)
- ResultScreen (resultados y estadísticas)
- QuestionEditorScreen (crear/editar preguntas)
- SettingsScreen (idioma, preferencias)

**Total**: 6 pantallas principales

---

## 🎯 Conclusiones

### Fortalezas del Diseño Propuesto

1. ✅ **Jerarquía flexible**: Folders anidables infinitamente mediante self-reference
2. ✅ **Integridad referencial**: Todas las relaciones con Foreign Keys y CASCADE DELETE
3. ✅ **Multiidioma escalable**: Campos directos en entidades para rendimiento óptimo
4. ✅ **Separación de concerns**: Arquitectura limpia con capas bien definidas
5. ✅ **Extensible**: Fácil agregar nuevos idiomas, dificultades, o tipos de preguntas
6. ✅ **Modo Study vs Game**: Soportado mediante campo `position` en options
7. ✅ **Auditoría**: Timestamps en todas las entidades principales

### Consideraciones Importantes

1. ⚠️ **Migraciones**: Al estar en versión 1, es el momento ideal para hacer estos cambios masivos
2. ⚠️ **Testing**: Fundamental probar exhaustivamente las cascadas de eliminación
3. ⚠️ **Validación**: Implementar validaciones a nivel de repositorio para prevenir ciclos en folders
4. ⚠️ **Performance**: Queries recursivas en folders pueden ser costosas; considerar límite de profundidad
5. ⚠️ **Backup**: Implementar exportación/importación de datos antes de producción

### Próximos Pasos Inmediatos

1. **Decidir y aprobar** el diseño de entidades propuesto
2. **Implementar Sprint 1** (Base de Datos) como fundación
3. **Crear tests** para cada componente antes de avanzar al siguiente
4. **Revisar** después de cada sprint para ajustar el plan si es necesario

---

## 📚 Referencias

- [Room Database - Android Developers](https://developer.android.com/training/data-storage/room)
- [Foreign Keys and Cascades - SQLite](https://www.sqlite.org/foreignkeys.html)
- [Compose Navigation](https://developer.android.com/jetpack/compose/navigation)
- [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android)
- [Android Localization](https://developer.android.com/guide/topics/resources/localization)

---

**Documento generado**: 2026-01-25
**Versión de la app**: 0.1.0-alpha (pre-implementación)
**Estado del proyecto**: Fase de diseño
