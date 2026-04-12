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
            seedLocalUser(db)
            seedDemoAttemptsAndAnalytics(db)

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
            FolderSeed("folder-math", "Matemáticas", null, "#0075DF", "calculate"),
            FolderSeed("folder-science", "Ciencias", null, "#00CBA5", "science"),
            FolderSeed("folder-history", "Historia", null, "#F8D970", "history"),
            FolderSeed("folder-languages", "Idiomas", null, "#60727F", "lang"),

            // Nested folders under Matemáticas
            FolderSeed("folder-algebra", "Álgebra", "folder-math", "#0075DF", "calculate"),
            FolderSeed("folder-geometry", "Geometría", "folder-math", "#00CBA5", "school"),
            FolderSeed("folder-calculus", "Cálculo", "folder-math", "#F8D970", "math"),

            // Nested folders under Ciencias
            FolderSeed("folder-physics", "Física", "folder-science", "#0075DF", "science"),
            FolderSeed("folder-biology", "Biología", "folder-science", "#00CBA5", "bacteria"),
            FolderSeed("folder-chemistry", "Química", "folder-science", "#E74C3C", "science"),

            // Nested folders under Historia
            FolderSeed("folder-ancient-history", "Historia Antigua", "folder-history", "#F8D970", "shelf"),

            // Nested folders under Idiomas
            FolderSeed("folder-english", "English", "folder-languages", "#60727F", "lang")
        )

        folders.forEach { folder ->
            db.execSQL(
                """
                INSERT OR IGNORE INTO folders (id, name, parentId, colorHex, icon, createdAt, updatedAt)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """,
                arrayOf(folder.id, folder.name, folder.parentId, folder.colorHex, folder.icon, now(), now())
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
                "calculate",
                "#0075DF"
            ),
            Pack(
                "pack-quadratic-eq",
                "Ecuaciones Cuadráticas",
                "Practica la fórmula general y factorización",
                "folder-algebra",
                "math",
                "#00CBA5"
            ),
            Pack(
                "pack-triangles",
                "Triángulos",
                "Teorema de Pitágoras y propiedades de triángulos",
                "folder-geometry",
                "history",
                "#F8D970"
            ),
            Pack(
                "pack-derivatives",
                "Derivadas Básicas",
                "Reglas introductorias de derivación",
                "folder-calculus",
                "math",
                "#0075DF"
            ),

            // Science packs
            Pack(
                "pack-newton-laws",
                "Leyes de Newton",
                "Tres leyes fundamentales del movimiento",
                "folder-physics",
                "science",
                "#00CBA5"
            ),
            Pack(
                "pack-cell-biology",
                "Biología Celular",
                "Estructura y funciones de la célula",
                "folder-biology",
                "bacteria",
                "#00CBA5"
            ),
            Pack(
                "pack-chemical-bonds",
                "Enlaces Químicos",
                "Tipos de enlaces y propiedades",
                "folder-chemistry",
                "science",
                "#E74C3C"
            ),

            // History packs
            Pack(
                "pack-rome",
                "Imperio Romano",
                "Expansión, política y legado de Roma",
                "folder-ancient-history",
                "shelf",
                "#F8D970"
            ),

            // Languages packs
            Pack(
                "pack-english-grammar",
                "English Grammar Basics",
                "Tiempos verbales, artículos y preposiciones",
                "folder-english",
                "lang",
                "#60727F"
            )
        )

        packs.forEach { pack ->
            db.execSQL(
                """
                INSERT OR IGNORE INTO packs (id, title, description, folderId, icon, colorHex, createdAt, updatedAt)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """,
                arrayOf(pack.id, pack.title, pack.description, pack.folderId, pack.icon, pack.colorHex, now(), now())
            )
        }
    }

    /**
     * Seed questions with options (Markdown supported)
     */
    private fun seedQuestionsAndOptions(db: SupportSQLiteDatabase) {
        val questions = listOf(
            SeedQuestion(
                id = "question-1",
                text = "Resuelve la ecuación: `2x + 3 = 7`",
                explanation = "Resta 3 a ambos lados: `2x = 4`. Luego divide entre 2: `x = 2`",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-1-a", "question-1", "A", "`x = 1`", false),
                    Option("opt-1-b", "question-1", "B", "`x = 2`", true),
                    Option("opt-1-c", "question-1", "C", "`x = 3`", false),
                    Option("opt-1-d", "question-1", "D", "`x = 5`", false)
                )
            ),
            SeedQuestion(
                id = "question-2",
                text = "¿Cuál es la solución de `x² - 5x + 6 = 0`?",
                explanation = "Factorizando: `(x - 2)(x - 3) = 0`, entonces `x = 2` o `x = 3`",
                difficulty = "MEDIUM",
                options = listOf(
                    Option("opt-2-a", "question-2", "A", "`x = 1` o `x = 6`", false),
                    Option("opt-2-b", "question-2", "B", "`x = 2` o `x = 3`", true),
                    Option("opt-2-c", "question-2", "C", "`x = -2` o `x = -3`", false),
                    Option("opt-2-d", "question-2", "D", "`x = 5` o `x = 6`", false)
                )
            ),
            SeedQuestion(
                id = "question-3",
                text = "Un triángulo rectángulo tiene catetos de 3 cm y 4 cm. ¿Cuánto mide la hipotenusa?",
                explanation = "Teorema de Pitágoras: `c² = a² + b²` → `c² = 9 + 16 = 25` → `c = 5 cm`",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-3-a", "question-3", "A", "5 cm", true),
                    Option("opt-3-b", "question-3", "B", "6 cm", false),
                    Option("opt-3-c", "question-3", "C", "7 cm", false),
                    Option("opt-3-d", "question-3", "D", "8 cm", false)
                )
            ),
            SeedQuestion(
                id = "question-4",
                text = "¿Qué establece la **Primera Ley de Newton**?",
                explanation = "Un objeto en reposo permanece en reposo y un objeto en movimiento permanece en movimiento a velocidad constante, a menos que actúe una fuerza externa.",
                difficulty = "MEDIUM",
                options = listOf(
                    Option("opt-4-a", "question-4", "A", "F = ma", false),
                    Option("opt-4-b", "question-4", "B", "Acción y reacción", false),
                    Option("opt-4-c", "question-4", "C", "Inercia", true),
                    Option("opt-4-d", "question-4", "D", "Gravedad", false)
                )
            ),
            SeedQuestion(
                id = "question-5",
                text = "¿Qué orgánulo celular se encarga de la producción de energía?",
                explanation = "La **mitocondria** es conocida como la 'central energética' de la célula, produce ATP mediante respiración celular.",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-5-a", "question-5", "A", "Núcleo", false),
                    Option("opt-5-b", "question-5", "B", "Mitocondria", true),
                    Option("opt-5-c", "question-5", "C", "Ribosoma", false),
                    Option("opt-5-d", "question-5", "D", "Lisosoma", false)
                )
            ),
            SeedQuestion(
                id = "question-6",
                text = "Despeja `x`: `5x - 10 = 0`",
                explanation = "Suma 10 a ambos lados: `5x = 10`, luego divide entre 5.",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-6-a", "question-6", "A", "`x = 1`", false),
                    Option("opt-6-b", "question-6", "B", "`x = 2`", true),
                    Option("opt-6-c", "question-6", "C", "`x = 5`", false),
                    Option("opt-6-d", "question-6", "D", "`x = 10`", false)
                )
            ),
            SeedQuestion(
                id = "question-7",
                text = "Si `3(x - 1) = 12`, ¿cuál es el valor de `x`?",
                explanation = "Divide entre 3: `x - 1 = 4`, luego suma 1.",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-7-a", "question-7", "A", "`x = 3`", false),
                    Option("opt-7-b", "question-7", "B", "`x = 4`", false),
                    Option("opt-7-c", "question-7", "C", "`x = 5`", true),
                    Option("opt-7-d", "question-7", "D", "`x = 6`", false)
                )
            ),
            SeedQuestion(
                id = "question-8",
                text = "Resuelve: `4x + 2 = 3x + 8`",
                explanation = "Pasa `3x` al lado izquierdo y `2` al derecho: `x = 6`.",
                difficulty = "MEDIUM",
                options = listOf(
                    Option("opt-8-a", "question-8", "A", "`x = 5`", false),
                    Option("opt-8-b", "question-8", "B", "`x = 6`", true),
                    Option("opt-8-c", "question-8", "C", "`x = 8`", false),
                    Option("opt-8-d", "question-8", "D", "`x = 10`", false)
                )
            ),
            SeedQuestion(
                id = "question-9",
                text = "¿Cuál es el discriminante de `x² + 4x + 4 = 0`?",
                explanation = "Para `ax² + bx + c`, `Δ = b² - 4ac = 16 - 16 = 0`.",
                difficulty = "MEDIUM",
                options = listOf(
                    Option("opt-9-a", "question-9", "A", "0", true),
                    Option("opt-9-b", "question-9", "B", "4", false),
                    Option("opt-9-c", "question-9", "C", "8", false),
                    Option("opt-9-d", "question-9", "D", "16", false)
                )
            ),
            SeedQuestion(
                id = "question-10",
                text = "¿Cuántas soluciones reales tiene `x² + 1 = 0`?",
                explanation = "El discriminante es negativo, así que no hay soluciones reales.",
                difficulty = "HARD",
                options = listOf(
                    Option("opt-10-a", "question-10", "A", "0", true),
                    Option("opt-10-b", "question-10", "B", "1", false),
                    Option("opt-10-c", "question-10", "C", "2", false),
                    Option("opt-10-d", "question-10", "D", "Infinitas", false)
                )
            ),
            SeedQuestion(
                id = "question-11",
                text = "Calcula la hipotenusa de un triángulo con catetos `5` y `12`.",
                explanation = "`c² = 5² + 12² = 25 + 144 = 169`, por tanto `c = 13`.",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-11-a", "question-11", "A", "11", false),
                    Option("opt-11-b", "question-11", "B", "12", false),
                    Option("opt-11-c", "question-11", "C", "13", true),
                    Option("opt-11-d", "question-11", "D", "14", false)
                )
            ),
            SeedQuestion(
                id = "question-12",
                text = "En un triángulo rectángulo, si la hipotenusa es `10` y un cateto es `6`, ¿cuánto mide el otro cateto?",
                explanation = "`b² = 10² - 6² = 100 - 36 = 64`, luego `b = 8`.",
                difficulty = "MEDIUM",
                options = listOf(
                    Option("opt-12-a", "question-12", "A", "6", false),
                    Option("opt-12-b", "question-12", "B", "7", false),
                    Option("opt-12-c", "question-12", "C", "8", true),
                    Option("opt-12-d", "question-12", "D", "9", false)
                )
            ),
            SeedQuestion(
                id = "question-13",
                text = "¿Qué tipo de triángulo tiene lados `3`, `4` y `5`?",
                explanation = "Cumple `3² + 4² = 5²`, por lo tanto es rectángulo.",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-13-a", "question-13", "A", "Equilátero", false),
                    Option("opt-13-b", "question-13", "B", "Isósceles", false),
                    Option("opt-13-c", "question-13", "C", "Rectángulo", true),
                    Option("opt-13-d", "question-13", "D", "Obtusángulo", false)
                )
            ),
            SeedQuestion(
                id = "question-14",
                text = "La derivada de `x²` es...",
                explanation = "Aplicando la regla de la potencia: `(x^n)' = n·x^(n-1)`.",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-14-a", "question-14", "A", "`x`", false),
                    Option("opt-14-b", "question-14", "B", "`2x`", true),
                    Option("opt-14-c", "question-14", "C", "`x²`", false),
                    Option("opt-14-d", "question-14", "D", "`2`", false)
                )
            ),
            SeedQuestion(
                id = "question-15",
                text = "La derivada de una constante es...",
                explanation = "Toda constante tiene derivada igual a `0`.",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-15-a", "question-15", "A", "0", true),
                    Option("opt-15-b", "question-15", "B", "1", false),
                    Option("opt-15-c", "question-15", "C", "`x`", false),
                    Option("opt-15-d", "question-15", "D", "No existe", false)
                )
            ),
            SeedQuestion(
                id = "question-16",
                text = "La derivada de `3x³` es...",
                explanation = "Multiplica por el exponente y resta 1: `3·3x² = 9x²`.",
                difficulty = "MEDIUM",
                options = listOf(
                    Option("opt-16-a", "question-16", "A", "`6x²`", false),
                    Option("opt-16-b", "question-16", "B", "`9x²`", true),
                    Option("opt-16-c", "question-16", "C", "`9x`", false),
                    Option("opt-16-d", "question-16", "D", "`x³`", false)
                )
            ),
            SeedQuestion(
                id = "question-17",
                text = "Según la **Segunda Ley de Newton**, la fuerza es...",
                explanation = "La fuerza neta sobre un cuerpo es igual a masa por aceleración.",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-17-a", "question-17", "A", "`F = ma`", true),
                    Option("opt-17-b", "question-17", "B", "`F = m / a`", false),
                    Option("opt-17-c", "question-17", "C", "`F = a / m`", false),
                    Option("opt-17-d", "question-17", "D", "`F = mv`", false)
                )
            ),
            SeedQuestion(
                id = "question-18",
                text = "Si un objeto no tiene fuerza neta actuando sobre él, su aceleración es...",
                explanation = "Sin fuerza neta, la aceleración es `0`.",
                difficulty = "MEDIUM",
                options = listOf(
                    Option("opt-18-a", "question-18", "A", "0", true),
                    Option("opt-18-b", "question-18", "B", "1", false),
                    Option("opt-18-c", "question-18", "C", "9.8", false),
                    Option("opt-18-d", "question-18", "D", "Depende de la masa", false)
                )
            ),
            SeedQuestion(
                id = "question-19",
                text = "La unidad de fuerza en el SI es el...",
                explanation = "La unidad es el **newton** (`N`).",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-19-a", "question-19", "A", "Julio", false),
                    Option("opt-19-b", "question-19", "B", "Pascal", false),
                    Option("opt-19-c", "question-19", "C", "Newton", true),
                    Option("opt-19-d", "question-19", "D", "Vatio", false)
                )
            ),
            SeedQuestion(
                id = "question-20",
                text = "¿Qué estructura contiene el material genético principal de la célula?",
                explanation = "El **núcleo** contiene la mayor parte del ADN en células eucariotas.",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-20-a", "question-20", "A", "Mitocondria", false),
                    Option("opt-20-b", "question-20", "B", "Núcleo", true),
                    Option("opt-20-c", "question-20", "C", "Membrana", false),
                    Option("opt-20-d", "question-20", "D", "Citoplasma", false)
                )
            ),
            SeedQuestion(
                id = "question-21",
                text = "Los ribosomas participan principalmente en la...",
                explanation = "Los ribosomas sintetizan proteínas a partir del ARN mensajero.",
                difficulty = "MEDIUM",
                options = listOf(
                    Option("opt-21-a", "question-21", "A", "Síntesis de proteínas", true),
                    Option("opt-21-b", "question-21", "B", "Producción de ATP", false),
                    Option("opt-21-c", "question-21", "C", "Digestión celular", false),
                    Option("opt-21-d", "question-21", "D", "Fotosíntesis", false)
                )
            ),
            SeedQuestion(
                id = "question-22",
                text = "¿Qué tipo de enlace comparten electrones entre átomos?",
                explanation = "En el enlace covalente los átomos comparten pares de electrones.",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-22-a", "question-22", "A", "Iónico", false),
                    Option("opt-22-b", "question-22", "B", "Covalente", true),
                    Option("opt-22-c", "question-22", "C", "Metálico", false),
                    Option("opt-22-d", "question-22", "D", "Puente de hidrógeno", false)
                )
            ),
            SeedQuestion(
                id = "question-23",
                text = "Un enlace iónico se forma típicamente entre...",
                explanation = "Suele formarse entre un metal y un no metal por transferencia de electrones.",
                difficulty = "MEDIUM",
                options = listOf(
                    Option("opt-23-a", "question-23", "A", "Dos no metales", false),
                    Option("opt-23-b", "question-23", "B", "Dos gases nobles", false),
                    Option("opt-23-c", "question-23", "C", "Un metal y un no metal", true),
                    Option("opt-23-d", "question-23", "D", "Dos metales", false)
                )
            ),
            SeedQuestion(
                id = "question-24",
                text = "¿Quién fue el primer emperador romano?",
                explanation = "**Augusto** consolidó el poder imperial tras el fin de la República.",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-24-a", "question-24", "A", "Julio César", false),
                    Option("opt-24-b", "question-24", "B", "Augusto", true),
                    Option("opt-24-c", "question-24", "C", "Nerón", false),
                    Option("opt-24-d", "question-24", "D", "Trajano", false)
                )
            ),
            SeedQuestion(
                id = "question-25",
                text = "¿Qué idioma era predominante en la parte occidental del Imperio Romano?",
                explanation = "En la parte occidental predominaba el **latín**.",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-25-a", "question-25", "A", "Griego", false),
                    Option("opt-25-b", "question-25", "B", "Latín", true),
                    Option("opt-25-c", "question-25", "C", "Egipcio", false),
                    Option("opt-25-d", "question-25", "D", "Fenicio", false)
                )
            ),
            SeedQuestion(
                id = "question-26",
                text = "Choose the correct sentence in English.",
                explanation = "La forma correcta usa el verbo `go` en presente simple con `she`.",
                difficulty = "EASY",
                options = listOf(
                    Option("opt-26-a", "question-26", "A", "She go to school every day.", false),
                    Option("opt-26-b", "question-26", "B", "She goes to school every day.", true),
                    Option("opt-26-c", "question-26", "C", "She going to school every day.", false),
                    Option("opt-26-d", "question-26", "D", "She gone to school every day.", false)
                )
            ),
            SeedQuestion(
                id = "question-27",
                text = "Fill in the blank: `I have lived here __ 2020.`",
                explanation = "Se usa `since` para un punto concreto en el tiempo.",
                difficulty = "MEDIUM",
                options = listOf(
                    Option("opt-27-a", "question-27", "A", "for", false),
                    Option("opt-27-b", "question-27", "B", "since", true),
                    Option("opt-27-c", "question-27", "C", "from", false),
                    Option("opt-27-d", "question-27", "D", "during", false)
                )
            )
        )

        questions.forEach { question ->
            insertQuestion(db, question)
        }
    }

    /**
     * Link questions to packs
     */
    private fun seedPackQuestions(db: SupportSQLiteDatabase) {
        val links = listOf(
            // Linear equations pack
            Triple("pack-linear-eq", "question-1", 0),
            Triple("pack-linear-eq", "question-6", 1),
            Triple("pack-linear-eq", "question-7", 2),
            Triple("pack-linear-eq", "question-8", 3),

            // Quadratic equations pack
            Triple("pack-quadratic-eq", "question-2", 0),
            Triple("pack-quadratic-eq", "question-9", 1),
            Triple("pack-quadratic-eq", "question-10", 2),

            // Triangles pack
            Triple("pack-triangles", "question-3", 0),
            Triple("pack-triangles", "question-11", 1),
            Triple("pack-triangles", "question-12", 2),
            Triple("pack-triangles", "question-13", 3),

            // Derivatives pack
            Triple("pack-derivatives", "question-14", 0),
            Triple("pack-derivatives", "question-15", 1),
            Triple("pack-derivatives", "question-16", 2),

            // Newton's laws pack
            Triple("pack-newton-laws", "question-4", 0),
            Triple("pack-newton-laws", "question-17", 1),
            Triple("pack-newton-laws", "question-18", 2),
            Triple("pack-newton-laws", "question-19", 3),

            // Cell biology pack
            Triple("pack-cell-biology", "question-5", 0),
            Triple("pack-cell-biology", "question-20", 1),
            Triple("pack-cell-biology", "question-21", 2),

            // Chemical bonds pack
            Triple("pack-chemical-bonds", "question-22", 0),
            Triple("pack-chemical-bonds", "question-23", 1),

            // Roman Empire pack
            Triple("pack-rome", "question-24", 0),
            Triple("pack-rome", "question-25", 1),

            // English grammar pack
            Triple("pack-english-grammar", "question-26", 0),
            Triple("pack-english-grammar", "question-27", 1)
        )

        links.forEach { (packId, questionId, sortOrder) ->
            db.execSQL(
                """
                INSERT OR IGNORE INTO pack_questions (packId, questionId, sortOrder)
                VALUES (?, ?, ?)
                """,
                arrayOf(packId, questionId, sortOrder)
            )
        }
    }

    private fun seedLocalUser(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            INSERT OR IGNORE INTO user_profiles (id, displayName, avatarIcon, avatarImageUri, createdAt, updatedAt)
            VALUES (?, ?, ?, ?, ?, ?)
            """,
            arrayOf("local-default-user", "Alex", null, null, now(), now())
        )
    }

    private fun seedDemoAttemptsAndAnalytics(db: SupportSQLiteDatabase) {
        val now = now()
        val twoHours = 2L * 60L * 60L * 1000L
        val oneDay = 24L * 60L * 60L * 1000L

        val completedAttempts = listOf(
            AttemptSeed(
                id = "attempt-cell-study-1",
                userId = "local-default-user",
                mode = "STUDY",
                status = "COMPLETED",
                primaryPackId = "pack-cell-biology",
                totalQuestions = 3,
                correctAnswers = 2,
                score = 67,
                startedAt = now - (5 * oneDay),
                completedAt = now - (5 * oneDay) + 11 * 60_000L,
                durationMs = 11 * 60_000L
            ),
            AttemptSeed(
                id = "attempt-cell-study-2",
                userId = "local-default-user",
                mode = "STUDY",
                status = "COMPLETED",
                primaryPackId = "pack-cell-biology",
                totalQuestions = 3,
                correctAnswers = 2,
                score = 67,
                startedAt = now - (2 * oneDay),
                completedAt = now - (2 * oneDay) + 12 * 60_000L,
                durationMs = 12 * 60_000L
            ),
            AttemptSeed(
                id = "attempt-cell-study-3",
                userId = "local-default-user",
                mode = "STUDY",
                status = "COMPLETED",
                primaryPackId = "pack-cell-biology",
                totalQuestions = 3,
                correctAnswers = 3,
                score = 100,
                startedAt = now - oneDay,
                completedAt = now - oneDay + 14 * 60_000L,
                durationMs = 14 * 60_000L
            ),
            AttemptSeed(
                id = "attempt-cell-game-1",
                userId = "local-default-user",
                mode = "GAME",
                status = "COMPLETED",
                primaryPackId = "pack-cell-biology",
                totalQuestions = 3,
                correctAnswers = 1,
                score = 61,
                startedAt = now - (3 * oneDay),
                completedAt = now - (3 * oneDay) + 4 * 60_000L,
                durationMs = 4 * 60_000L
            ),
            AttemptSeed(
                id = "attempt-linear-study-1",
                userId = "local-default-user",
                mode = "STUDY",
                status = "COMPLETED",
                primaryPackId = "pack-linear-eq",
                totalQuestions = 4,
                correctAnswers = 3,
                score = 75,
                startedAt = now - (7 * oneDay),
                completedAt = now - (7 * oneDay) + 9 * 60_000L,
                durationMs = 9 * 60_000L
            ),
            AttemptSeed(
                id = "attempt-rome-game-1",
                userId = "local-default-user",
                mode = "GAME",
                status = "COMPLETED",
                primaryPackId = "pack-rome",
                totalQuestions = 2,
                correctAnswers = 2,
                score = 92,
                startedAt = now - (4 * oneDay),
                completedAt = now - (4 * oneDay) + 2 * 60_000L,
                durationMs = 2 * 60_000L
            )
        )

        val activeAttempts = listOf(
            AttemptSeed(
                id = "attempt-linear-study-active",
                userId = "local-default-user",
                mode = "STUDY",
                status = "IN_PROGRESS",
                primaryPackId = "pack-linear-eq",
                totalQuestions = 4,
                correctAnswers = 0,
                score = 0,
                startedAt = now - twoHours,
                completedAt = null,
                durationMs = null,
                currentQuestionIndex = 2
            )
        )

        (completedAttempts + activeAttempts).forEach { attempt ->
            db.execSQL(
                """
                INSERT OR IGNORE INTO attempts
                (id, userId, mode, status, startedAt, completedAt, durationMs, score, primaryPackId, totalQuestions, correctAnswers, currentQuestionIndex, createdAt, updatedAt)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                arrayOf(
                    attempt.id,
                    attempt.userId,
                    attempt.mode,
                    attempt.status,
                    attempt.startedAt,
                    attempt.completedAt,
                    attempt.durationMs,
                    attempt.score,
                    attempt.primaryPackId,
                    attempt.totalQuestions,
                    attempt.correctAnswers,
                    attempt.currentQuestionIndex,
                    attempt.startedAt,
                    attempt.completedAt ?: attempt.startedAt
                )
            )
        }

        val answers = listOf(
            AttemptAnswerSeed("ans-cell-s1-q5", "attempt-cell-study-1", "question-5", "opt-5-b", true, 26_000L, null, now - (5 * oneDay) + 1_000L),
            AttemptAnswerSeed("ans-cell-s1-q20", "attempt-cell-study-1", "question-20", "opt-20-a", true, 31_000L, null, now - (5 * oneDay) + 2_000L),
            AttemptAnswerSeed("ans-cell-s1-q21", "attempt-cell-study-1", "question-21", "opt-21-a", false, 39_000L, null, now - (5 * oneDay) + 3_000L),
            AttemptAnswerSeed("ans-cell-s2-q5", "attempt-cell-study-2", "question-5", "opt-5-b", true, 25_000L, null, now - (2 * oneDay) + 1_000L),
            AttemptAnswerSeed("ans-cell-s2-q20", "attempt-cell-study-2", "question-20", "opt-20-b", false, 34_000L, null, now - (2 * oneDay) + 2_000L),
            AttemptAnswerSeed("ans-cell-s2-q21", "attempt-cell-study-2", "question-21", "opt-21-b", true, 28_000L, null, now - (2 * oneDay) + 3_000L),
            AttemptAnswerSeed("ans-cell-s3-q5", "attempt-cell-study-3", "question-5", "opt-5-b", true, 22_000L, null, now - oneDay + 1_000L),
            AttemptAnswerSeed("ans-cell-s3-q20", "attempt-cell-study-3", "question-20", "opt-20-a", true, 27_000L, null, now - oneDay + 2_000L),
            AttemptAnswerSeed("ans-cell-s3-q21", "attempt-cell-study-3", "question-21", "opt-21-b", true, 24_000L, null, now - oneDay + 3_000L),
            AttemptAnswerSeed("ans-cell-g1-q5", "attempt-cell-game-1", "question-5", "opt-5-b", true, 7_000L, 15_000L, now - (3 * oneDay) + 1_000L),
            AttemptAnswerSeed("ans-cell-g1-q20", "attempt-cell-game-1", "question-20", "opt-20-b", false, 11_000L, 15_000L, now - (3 * oneDay) + 2_000L),
            AttemptAnswerSeed("ans-cell-g1-q21", "attempt-cell-game-1", "question-21", "opt-21-a", false, 13_000L, 15_000L, now - (3 * oneDay) + 3_000L),
            AttemptAnswerSeed("ans-linear-s1-q1", "attempt-linear-study-1", "question-1", "opt-1-b", true, 19_000L, null, now - (7 * oneDay) + 1_000L),
            AttemptAnswerSeed("ans-linear-s1-q6", "attempt-linear-study-1", "question-6", "opt-6-b", true, 18_000L, null, now - (7 * oneDay) + 2_000L),
            AttemptAnswerSeed("ans-linear-s1-q7", "attempt-linear-study-1", "question-7", "opt-7-c", true, 22_000L, null, now - (7 * oneDay) + 3_000L),
            AttemptAnswerSeed("ans-linear-s1-q8", "attempt-linear-study-1", "question-8", "opt-8-a", false, 29_000L, null, now - (7 * oneDay) + 4_000L),
            AttemptAnswerSeed("ans-rome-g1-q24", "attempt-rome-game-1", "question-24", "opt-24-b", true, 6_500L, 12_000L, now - (4 * oneDay) + 1_000L),
            AttemptAnswerSeed("ans-rome-g1-q25", "attempt-rome-game-1", "question-25", "opt-25-c", true, 8_000L, 12_000L, now - (4 * oneDay) + 2_000L),
            AttemptAnswerSeed("ans-linear-active-q1", "attempt-linear-study-active", "question-1", "opt-1-b", true, 20_000L, null, now - twoHours + 1_000L),
            AttemptAnswerSeed("ans-linear-active-q6", "attempt-linear-study-active", "question-6", "opt-6-a", false, 25_000L, null, now - twoHours + 2_000L)
        )

        answers.forEach { answer ->
            db.execSQL(
                """
                INSERT OR IGNORE INTO attempt_answers
                (id, attemptId, questionId, pickedOptionId, isCorrect, timeMs, timeLimitMs, createdAt, updatedAt)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                arrayOf(
                    answer.id,
                    answer.attemptId,
                    answer.questionId,
                    answer.pickedOptionId,
                    if (answer.isCorrect) 1 else 0,
                    answer.timeMs,
                    answer.timeLimitMs,
                    answer.createdAt,
                    answer.createdAt
                )
            )
        }

        val questionStats = listOf(
            QuestionStatsSeed("local-default-user:question-5", "local-default-user", "question-5", "pack-cell-biology", 4, 4, 0, 0, 3, 3, 1, 1, 7_000L, 7_000L, 4, 4, 1.0f, "MASTERED", now - oneDay),
            QuestionStatsSeed("local-default-user:question-20", "local-default-user", "question-20", "pack-cell-biology", 4, 2, 2, 0, 3, 2, 1, 0, 11_000L, 11_000L, 1, 2, 0.58f, "PRACTICED", now - oneDay),
            QuestionStatsSeed("local-default-user:question-21", "local-default-user", "question-21", "pack-cell-biology", 4, 2, 2, 0, 3, 2, 1, 0, 13_000L, 13_000L, 1, 1, 0.43f, "LEARNING", now - oneDay),
            QuestionStatsSeed("local-default-user:question-1", "local-default-user", "question-1", "pack-linear-eq", 2, 2, 0, 0, 2, 2, 0, 0, null, null, 2, 2, 0.90f, "MASTERED", now - twoHours),
            QuestionStatsSeed("local-default-user:question-6", "local-default-user", "question-6", "pack-linear-eq", 2, 1, 1, 0, 2, 1, 0, 0, null, null, 0, 1, 0.41f, "LEARNING", now - twoHours),
            QuestionStatsSeed("local-default-user:question-24", "local-default-user", "question-24", "pack-rome", 2, 2, 0, 0, 0, 0, 2, 2, 7_000L, 6_500L, 2, 2, 0.98f, "MASTERED", now - (90 * 60_000L)),
            QuestionStatsSeed("local-default-user:question-25", "local-default-user", "question-25", "pack-rome", 1, 1, 0, 0, 0, 0, 1, 1, 8_000L, 8_000L, 1, 1, 0.74f, "PRACTICED", now - (4 * oneDay))
        )

        questionStats.forEach { stat ->
            db.execSQL(
                """
                INSERT OR IGNORE INTO question_stats
                (id, userId, questionId, packId, totalAttempts, totalCorrect, totalIncorrect, totalTimeout, studyAttempts, studyCorrect, gameAttempts, gameCorrect, avgGameTimeMs, bestGameTimeMs, currentCorrectStreak, bestCorrectStreak, masteryScore, masteryLevel, lastAnsweredAt, createdAt, updatedAt)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                arrayOf(
                    stat.id,
                    stat.userId,
                    stat.questionId,
                    stat.packId,
                    stat.totalAttempts,
                    stat.totalCorrect,
                    stat.totalIncorrect,
                    stat.totalTimeout,
                    stat.studyAttempts,
                    stat.studyCorrect,
                    stat.gameAttempts,
                    stat.gameCorrect,
                    stat.avgGameTimeMs,
                    stat.bestGameTimeMs,
                    stat.currentCorrectStreak,
                    stat.bestCorrectStreak,
                    stat.masteryScore,
                    stat.masteryLevel,
                    stat.lastAnsweredAt,
                    stat.lastAnsweredAt ?: now,
                    stat.lastAnsweredAt ?: now
                )
            )
        }

        val packStats = listOf(
            PackStatsSeed(
                id = "local-default-user:pack-cell-biology",
                userId = "local-default-user",
                packId = "pack-cell-biology",
                totalSessions = 4,
                totalStudySessions = 3,
                totalGameSessions = 1,
                averageAccuracyPercent = 67,
                averageStudyAccuracyPercent = 78,
                averageGameAccuracyPercent = 33,
                averageDurationMs = 524_000L,
                averageStudyDurationMs = 740_000L,
                averageGameDurationMs = 240_000L,
                bestScore = 100,
                bestStudyAccuracyPercent = 100,
                bestGameScore = 61,
                lastSessionAt = now - oneDay + 14 * 60_000L,
                lastSessionMode = "STUDY",
                mostUsedMode = "STUDY",
                dominatedQuestions = 1,
                totalQuestionsSnapshot = 3,
                progressPercent = 33
            ),
            PackStatsSeed(
                id = "local-default-user:pack-linear-eq",
                userId = "local-default-user",
                packId = "pack-linear-eq",
                totalSessions = 1,
                totalStudySessions = 1,
                totalGameSessions = 0,
                averageAccuracyPercent = 75,
                averageStudyAccuracyPercent = 75,
                averageGameAccuracyPercent = null,
                averageDurationMs = 540_000L,
                averageStudyDurationMs = 540_000L,
                averageGameDurationMs = null,
                bestScore = 75,
                bestStudyAccuracyPercent = 75,
                bestGameScore = null,
                lastSessionAt = now - (7 * oneDay) + 9 * 60_000L,
                lastSessionMode = "STUDY",
                mostUsedMode = "STUDY",
                dominatedQuestions = 1,
                totalQuestionsSnapshot = 4,
                progressPercent = 25
            ),
            PackStatsSeed(
                id = "local-default-user:pack-rome",
                userId = "local-default-user",
                packId = "pack-rome",
                totalSessions = 1,
                totalStudySessions = 0,
                totalGameSessions = 1,
                averageAccuracyPercent = 100,
                averageStudyAccuracyPercent = null,
                averageGameAccuracyPercent = 100,
                averageDurationMs = 120_000L,
                averageStudyDurationMs = null,
                averageGameDurationMs = 120_000L,
                bestScore = 92,
                bestStudyAccuracyPercent = null,
                bestGameScore = 92,
                lastSessionAt = now - (4 * oneDay) + 2 * 60_000L,
                lastSessionMode = "GAME",
                mostUsedMode = "GAME",
                dominatedQuestions = 1,
                totalQuestionsSnapshot = 2,
                progressPercent = 50
            )
        )

        packStats.forEach { stat ->
            db.execSQL(
                """
                INSERT OR IGNORE INTO pack_stats
                (id, userId, packId, totalSessions, totalStudySessions, totalGameSessions, averageAccuracyPercent, averageStudyAccuracyPercent, averageGameAccuracyPercent, averageDurationMs, averageStudyDurationMs, averageGameDurationMs, bestScore, bestStudyAccuracyPercent, bestGameScore, lastSessionAt, lastSessionMode, mostUsedMode, dominatedQuestions, totalQuestionsSnapshot, progressPercent, createdAt, updatedAt)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                arrayOf(
                    stat.id,
                    stat.userId,
                    stat.packId,
                    stat.totalSessions,
                    stat.totalStudySessions,
                    stat.totalGameSessions,
                    stat.averageAccuracyPercent,
                    stat.averageStudyAccuracyPercent,
                    stat.averageGameAccuracyPercent,
                    stat.averageDurationMs,
                    stat.averageStudyDurationMs,
                    stat.averageGameDurationMs,
                    stat.bestScore,
                    stat.bestStudyAccuracyPercent,
                    stat.bestGameScore,
                    stat.lastSessionAt,
                    stat.lastSessionMode,
                    stat.mostUsedMode,
                    stat.dominatedQuestions,
                    stat.totalQuestionsSnapshot,
                    stat.progressPercent,
                    now,
                    now
                )
            )
        }

        db.execSQL(
            """
            INSERT OR IGNORE INTO user_rank
            (userId, currentRank, mmr, perfEwma, lifetimeCorrect, lifetimeIncorrect, lifetimeTimeout, totalGameAnswers, totalStudyAnswers, lastRankChangeAt, answersSinceRankChange, totalXp, createdAt, updatedAt)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """,
            arrayOf(
                "local-default-user",
                "ACOLYTE",
                1345f,
                0.68f,
                12,
                5,
                0,
                6,
                11,
                now - (3 * oneDay),
                7,
                860L,
                now,
                now
            )
        )
    }

    /**
     * Helper: Insert multiple options
     */
    private fun insertOptions(db: SupportSQLiteDatabase, options: List<Option>) {
        options.forEach { opt ->
            db.execSQL(
                """
                INSERT OR IGNORE INTO options (id, questionId, label, text, isCorrect, createdAt, updatedAt)
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

    private fun insertQuestion(db: SupportSQLiteDatabase, question: SeedQuestion) {
        db.execSQL(
            """
            INSERT OR IGNORE INTO questions (id, text, explanation, difficulty, createdAt, updatedAt)
            VALUES (?, ?, ?, ?, ?, ?)
            """,
            arrayOf(
                question.id,
                question.text,
                question.explanation,
                question.difficulty,
                now(),
                now()
            )
        )
        insertOptions(db, question.options)
    }

    // Data classes for seeding
    private data class FolderSeed(
        val id: String,
        val name: String,
        val parentId: String?,
        val colorHex: String,
        val icon: String?
    )

    private data class Pack(
        val id: String,
        val title: String,
        val description: String,
        val folderId: String,
        val icon: String?,
        val colorHex: String
    )

    private data class Option(
        val id: String,
        val questionId: String,
        val label: String,
        val text: String,
        val isCorrect: Boolean
    )

    private data class SeedQuestion(
        val id: String,
        val text: String,
        val explanation: String,
        val difficulty: String,
        val options: List<Option>
    )

    private data class AttemptSeed(
        val id: String,
        val userId: String,
        val mode: String,
        val status: String,
        val primaryPackId: String,
        val totalQuestions: Int,
        val correctAnswers: Int,
        val score: Int,
        val startedAt: Long,
        val completedAt: Long?,
        val durationMs: Long?,
        val currentQuestionIndex: Int = 0
    )

    private data class AttemptAnswerSeed(
        val id: String,
        val attemptId: String,
        val questionId: String,
        val pickedOptionId: String?,
        val isCorrect: Boolean,
        val timeMs: Long,
        val timeLimitMs: Long?,
        val createdAt: Long
    )

    private data class QuestionStatsSeed(
        val id: String,
        val userId: String,
        val questionId: String,
        val packId: String,
        val totalAttempts: Int,
        val totalCorrect: Int,
        val totalIncorrect: Int,
        val totalTimeout: Int,
        val studyAttempts: Int,
        val studyCorrect: Int,
        val gameAttempts: Int,
        val gameCorrect: Int,
        val avgGameTimeMs: Long?,
        val bestGameTimeMs: Long?,
        val currentCorrectStreak: Int,
        val bestCorrectStreak: Int,
        val masteryScore: Float,
        val masteryLevel: String,
        val lastAnsweredAt: Long
    )

    private data class PackStatsSeed(
        val id: String,
        val userId: String,
        val packId: String,
        val totalSessions: Int,
        val totalStudySessions: Int,
        val totalGameSessions: Int,
        val averageAccuracyPercent: Int?,
        val averageStudyAccuracyPercent: Int?,
        val averageGameAccuracyPercent: Int?,
        val averageDurationMs: Long?,
        val averageStudyDurationMs: Long?,
        val averageGameDurationMs: Long?,
        val bestScore: Int?,
        val bestStudyAccuracyPercent: Int?,
        val bestGameScore: Int?,
        val lastSessionAt: Long?,
        val lastSessionMode: String?,
        val mostUsedMode: String?,
        val dominatedQuestions: Int,
        val totalQuestionsSnapshot: Int,
        val progressPercent: Int
    )
}
