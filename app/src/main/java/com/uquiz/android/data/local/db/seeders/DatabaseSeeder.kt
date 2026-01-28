package com.uquiz.android.data.local.db.seeders

import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Database Seeder - Initial data for development
 *
 * Similar to Laravel/Rails seeders: separates schema migrations from data seeding
 */
object DatabaseSeeder {

    private fun now() = System.currentTimeMillis()

    /**
     * Seed database with initial test data
     */
    fun seed(db: SupportSQLiteDatabase) {
        db.beginTransaction()
        try {
            seedFolders(db)
            seedPacks(db)
            seedQuestionsAndOptions(db)
            seedPackQuestions(db)

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    /**
     * Seed folders with hierarchical structure
     */
    private fun seedFolders(db: SupportSQLiteDatabase) {
        val folders = listOf(
            // Root folders
            Triple("folder-math", "Matemáticas", null),
            Triple("folder-science", "Ciencias", null),
            Triple("folder-history", "Historia", null),

            // Nested folders under Matemáticas
            Triple("folder-algebra", "Álgebra", "folder-math"),
            Triple("folder-geometry", "Geometría", "folder-math"),

            // Nested folders under Ciencias
            Triple("folder-physics", "Física", "folder-science"),
            Triple("folder-biology", "Biología", "folder-science")
        )

        folders.forEach { (id, name, parentId) ->
            db.execSQL(
                """
                INSERT INTO folders (id, name, parentId, createdAt, updatedAt)
                VALUES (?, ?, ?, ?, ?)
                """,
                arrayOf(id, name, parentId, now(), now())
            )
        }
    }

    /**
     * Seed packs with descriptions and icons
     */
    private fun seedPacks(db: SupportSQLiteDatabase) {
        val packs = listOf(
            // Math packs
            Pack(
                "pack-linear-eq",
                "Ecuaciones Lineales",
                "Resuelve ecuaciones de primer grado con una incógnita",
                "folder-algebra",
                "calculate"
            ),
            Pack(
                "pack-quadratic-eq",
                "Ecuaciones Cuadráticas",
                "Practica la fórmula general y factorización",
                "folder-algebra",
                "function"
            ),
            Pack(
                "pack-triangles",
                "Triángulos",
                "Teorema de Pitágoras y propiedades de triángulos",
                "folder-geometry",
                "change_history"
            ),

            // Science packs
            Pack(
                "pack-newton-laws",
                "Leyes de Newton",
                "Tres leyes fundamentales del movimiento",
                "folder-physics",
                "science"
            ),
            Pack(
                "pack-cell-biology",
                "Biología Celular",
                "Estructura y funciones de la célula",
                "folder-biology",
                "biotech"
            )
        )

        packs.forEachIndexed { index, pack ->
            db.execSQL(
                """
                INSERT INTO packs (id, title, description, folderId, icon, createdAt, updatedAt)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """,
                arrayOf(pack.id, pack.title, pack.description, pack.folderId, pack.icon, now(), now())
            )
        }
    }

    /**
     * Seed questions with options (Markdown supported)
     */
    private fun seedQuestionsAndOptions(db: SupportSQLiteDatabase) {
        // Question 1: Linear equation
        val q1Id = "question-1"
        db.execSQL(
            """
            INSERT INTO questions (id, text, explanation, difficulty, createdAt, updatedAt)
            VALUES (?, ?, ?, ?, ?, ?)
            """,
            arrayOf(
                q1Id,
                "Resuelve la ecuación: `2x + 3 = 7`",
                "Resta 3 a ambos lados: `2x = 4`. Luego divide entre 2: `x = 2`",
                "EASY",
                now(),
                now()
            )
        )

        // Options for question 1
        val q1Options = listOf(
            Option("opt-1-a", q1Id, "A", "`x = 1`", false),
            Option("opt-1-b", q1Id, "B", "`x = 2`", true),  // Correct
            Option("opt-1-c", q1Id, "C", "`x = 3`", false),
            Option("opt-1-d", q1Id, "D", "`x = 5`", false)
        )
        insertOptions(db, q1Options)

        // Question 2: Quadratic equation
        val q2Id = "question-2"
        db.execSQL(
            """
            INSERT INTO questions (id, text, explanation, difficulty, createdAt, updatedAt)
            VALUES (?, ?, ?, ?, ?, ?)
            """,
            arrayOf(
                q2Id,
                "¿Cuál es la solución de `x² - 5x + 6 = 0`?",
                "Factorizando: `(x - 2)(x - 3) = 0`, entonces `x = 2` o `x = 3`",
                "MEDIUM",
                now(),
                now()
            )
        )

        val q2Options = listOf(
            Option("opt-2-a", q2Id, "A", "`x = 1` o `x = 6`", false),
            Option("opt-2-b", q2Id, "B", "`x = 2` o `x = 3`", true),  // Correct
            Option("opt-2-c", q2Id, "C", "`x = -2` o `x = -3`", false),
            Option("opt-2-d", q2Id, "D", "`x = 5` o `x = 6`", false)
        )
        insertOptions(db, q2Options)

        // Question 3: Pythagorean theorem
        val q3Id = "question-3"
        db.execSQL(
            """
            INSERT INTO questions (id, text, explanation, difficulty, createdAt, updatedAt)
            VALUES (?, ?, ?, ?, ?, ?)
            """,
            arrayOf(
                q3Id,
                "Un triángulo rectángulo tiene catetos de 3 cm y 4 cm. ¿Cuánto mide la hipotenusa?",
                "Teorema de Pitágoras: `c² = a² + b²` → `c² = 9 + 16 = 25` → `c = 5 cm`",
                "EASY",
                now(),
                now()
            )
        )

        val q3Options = listOf(
            Option("opt-3-a", q3Id, "A", "5 cm", true),  // Correct
            Option("opt-3-b", q3Id, "B", "6 cm", false),
            Option("opt-3-c", q3Id, "C", "7 cm", false),
            Option("opt-3-d", q3Id, "D", "8 cm", false)
        )
        insertOptions(db, q3Options)

        // Question 4: Newton's First Law
        val q4Id = "question-4"
        db.execSQL(
            """
            INSERT INTO questions (id, text, explanation, difficulty, createdAt, updatedAt)
            VALUES (?, ?, ?, ?, ?, ?)
            """,
            arrayOf(
                q4Id,
                "¿Qué establece la **Primera Ley de Newton**?",
                "Un objeto en reposo permanece en reposo y un objeto en movimiento permanece en movimiento a velocidad constante, a menos que actúe una fuerza externa.",
                "MEDIUM",
                now(),
                now()
            )
        )

        val q4Options = listOf(
            Option("opt-4-a", q4Id, "A", "F = ma", false),
            Option("opt-4-b", q4Id, "B", "Acción y reacción", false),
            Option("opt-4-c", q4Id, "C", "Inercia", true),  // Correct
            Option("opt-4-d", q4Id, "D", "Gravedad", false)
        )
        insertOptions(db, q4Options)

        // Question 5: Cell biology
        val q5Id = "question-5"
        db.execSQL(
            """
            INSERT INTO questions (id, text, explanation, difficulty, createdAt, updatedAt)
            VALUES (?, ?, ?, ?, ?, ?)
            """,
            arrayOf(
                q5Id,
                "¿Qué orgánulo celular se encarga de la producción de energía?",
                "La **mitocondria** es conocida como la 'central energética' de la célula, produce ATP mediante respiración celular.",
                "EASY",
                now(),
                now()
            )
        )

        val q5Options = listOf(
            Option("opt-5-a", q5Id, "A", "Núcleo", false),
            Option("opt-5-b", q5Id, "B", "Mitocondria", true),  // Correct
            Option("opt-5-c", q5Id, "C", "Ribosoma", false),
            Option("opt-5-d", q5Id, "D", "Lisosoma", false)
        )
        insertOptions(db, q5Options)
    }

    /**
     * Link questions to packs
     */
    private fun seedPackQuestions(db: SupportSQLiteDatabase) {
        val links = listOf(
            // Linear equations pack
            Triple("pack-linear-eq", "question-1", 0),

            // Quadratic equations pack
            Triple("pack-quadratic-eq", "question-2", 0),

            // Triangles pack
            Triple("pack-triangles", "question-3", 0),

            // Newton's laws pack
            Triple("pack-newton-laws", "question-4", 0),

            // Cell biology pack
            Triple("pack-cell-biology", "question-5", 0)
        )

        links.forEach { (packId, questionId, sortOrder) ->
            db.execSQL(
                """
                INSERT INTO pack_questions (packId, questionId, sortOrder)
                VALUES (?, ?, ?)
                """,
                arrayOf(packId, questionId, sortOrder)
            )
        }
    }

    /**
     * Helper: Insert multiple options
     */
    private fun insertOptions(db: SupportSQLiteDatabase, options: List<Option>) {
        options.forEach { opt ->
            db.execSQL(
                """
                INSERT INTO options (id, questionId, label, text, isCorrect, createdAt, updatedAt)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """,
                arrayOf(
                    opt.id,
                    opt.questionId,
                    opt.label,
                    opt.text,
                    if (opt.isCorrect) 1 else 0,
                    now(),
                    now()
                )
            )
        }
    }

    // Data classes for seeding
    private data class Pack(
        val id: String,
        val title: String,
        val description: String,
        val folderId: String,
        val icon: String?
    )

    private data class Option(
        val id: String,
        val questionId: String,
        val label: String,
        val text: String,
        val isCorrect: Boolean
    )
}
