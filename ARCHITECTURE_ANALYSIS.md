# UQuiz - Análisis de Arquitectura y Diseño MVP

## 📋 Tabla de Contenidos
1. [Resumen Ejecutivo](#resumen-ejecutivo)
2. [Objetivos del MVP](#objetivos-del-mvp)
3. [Estructura Actual del Proyecto](#estructura-actual-del-proyecto)
4. [Modelo de Datos Completo](#modelo-de-datos-completo)
5. [Diagrama Entidad-Relación](#diagrama-entidad-relación)
6. [Jerarquía de Contenidos](#jerarquía-de-contenidos)
7. [Sistema de Attempts y Stats](#sistema-de-attempts-y-stats)
8. [Modos de Juego](#modos-de-juego)
9. [Estrategia de Multiidioma](#estrategia-de-multiidioma)
10. [Soporte de Markdown](#soporte-de-markdown)
11. [Operaciones Principales y Cascadas](#operaciones-principales-y-cascadas)
12. [Contratos de Repositorios](#contratos-de-repositorios)
13. [Cambios Necesarios](#cambios-necesarios)
14. [Plan de Implementación](#plan-de-implementación)
15. [Alcance MVP](#alcance-mvp)

---

## 🎯 Resumen Ejecutivo

**UQuiz** es una app Android offline-first para crear y practicar preguntas tipo test con contenido en **Markdown**, organizadas en **Folders** y **Packs**, con dos modos de juego:

- **Study**: Práctica por pack con verificación inmediata y explicación
- **Game**: Modo arcade multi-pack con timer decreciente, puntuación y resumen

**Arquitectura**: MVVM + Repository + Room, UI con Jetpack Compose, estado inmutable con UDF (Unidirectional Data Flow)

**Estado Actual**: Proyecto en fase inicial con Room configurado, arquitectura limpia definida, pero sin implementación de UI, ViewModels, sistema de attempts, ni soporte para folders anidables.

---

## 📝 Objetivos del MVP

### Funcionalidades Core

1. ✅ **CRUD de Contenido**:
   - Folders anidables (2+ niveles)
   - Packs dentro de folders
   - Questions con opciones (Markdown)
   - Crear questions SOLO desde Pack (no hay "question library")

2. ✅ **Study Mode**:
   - Selección de 1 pack
   - Sesión secuencial con navegación
   - Verificación inmediata + panel de explicación
   - Guardar intentos y respuestas para stats

3. ✅ **Game Mode**:
   - Selección multi-pack
   - Deduplicación de questions
   - Temporizador decreciente por pregunta
   - Sistema de puntuación
   - Resumen final

4. ✅ **Stats**:
   - Panel global + por pack
   - Sesiones, accuracy, tiempo medio
   - Progreso por pack

5. ✅ **Import/Export**:
   - JSON básico para packs con validación

### Fuera del MVP

- ❌ Cuentas de usuario
- ❌ Sincronización cloud
- ❌ Comunidad/sharing
- ❌ IA para generar preguntas
- ❌ Rankings online
- ❌ Badges/achievements

---

## 📁 Estructura Actual del Proyecto

### Arquitectura de Capas

```
com.uquiz.android/
├── core/di/                    # ❌ VACÍO - DI no configurado
├── data/
│   ├── local/
│   │   ├── dao/                # ✅ QuestionDao, PackDao (básicos)
│   │   ├── db/                 # ✅ UQuizDatabase, Converters
│   │   ├── entity/             # ⚠️ 4 entidades (faltan 4 más)
│   │   ├── mapper/             # ❌ VACÍO - Sin mappers
│   │   └── relation/           # ✅ OrderedQuestionWithOptions
│   └── repository/             # ❌ VACÍO - Sin implementaciones
├── domain/
│   ├── model/                  # ⚠️ Question, Pack, Option (incompletos)
│   └── repository/             # ⚠️ Solo 2 interfaces (faltan 5)
└── ui/
    ├── navigation/             # ❌ VACÍO - Sin navegación
    └── theme/                  # ✅ Material3 theme configurado
```

### Análisis de Entidades Existentes

#### ❌ Problemas Críticos:

1. **QuestionEntity**:
   - ✅ Tiene `text` y `correctOptionIds`
   - ❌ Sin campo `explanation` (Markdown)
   - ❌ Sin campo `difficulty`
   - ❌ Sin timestamps
   - ❌ Campo `correctOptionIds` NO ES CORRECTO (MVP: 1 correcta, va en Option.isCorrect)

2. **OptionEntity**:
   - ✅ Tiene `text` y FK a Question con CASCADE
   - ❌ Sin campo `label` (A/B/C/D)
   - ❌ Sin campo `isCorrect` (boolean)
   - ❌ Sin campo `position` (orden para Study mode)

3. **PackEntity**:
   - ✅ Tiene `name`
   - ❌ Sin campo `description`
   - ❌ Sin campo `folderId` (relación con Folder)
   - ❌ Sin timestamps

4. **PackQuestionEntity**:
   - ✅ Tiene composite PK y `position`
   - 🚨 **SIN Foreign Keys**: Riesgo de registros huérfanos

5. **FolderEntity**:
   - ❌ **NO EXISTE**

6. **AttemptEntity, AttemptPackEntity, AttemptAnswerEntity**:
   - ❌ **NO EXISTEN** - Sin sistema de tracking

---

## 🗄️ Modelo de Datos Completo

### Entidades Necesarias (8 totales)

#### 1. Organización de Contenido (4 entidades)

- **FolderEntity**: Carpetas anidables
- **PackEntity**: Paquetes de preguntas
- **QuestionEntity**: Preguntas individuales
- **OptionEntity**: Opciones de respuesta
- **PackQuestionEntity**: Relación M:N (junction table)

#### 2. Sistema de Attempts (3 entidades)

- **AttemptEntity**: Sesiones Study/Game
- **AttemptPackEntity**: Packs en sesión Game
- **AttemptAnswerEntity**: Respuestas individuales

---

## 🏗️ Diseño de Entidades

### 1. FolderEntity (NUEVA)

```kotlin
@Entity(
    tableName = "folders",
    foreignKeys = [
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("parentId")]
)
data class FolderEntity(
    @PrimaryKey val id: String,
    val name: String,                        // En idioma del usuario
    val parentId: String? = null,            // null = raíz
    val position: Int = 0,                   // Orden en el nivel
    val createdAt: Long = System.currentTimeMillis()
)
```

**Características**:
- ✅ Self-reference para anidamiento infinito
- ✅ CASCADE DELETE elimina jerarquía completa
- ✅ Contenido del usuario (NO multiidioma aquí)

### 2. PackEntity (MODIFICADA)

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
    val title: String,                       // En idioma del usuario
    val description: String? = null,         // En idioma del usuario
    val folderId: String,                    // NOT NULL - siempre en folder
    val position: Int = 0,
    val color: String? = null,               // Hex color (#RRGGBB)
    val icon: String? = null,                // Nombre de ícono
    val createdAt: Long = System.currentTimeMillis()
)
```

**Cambios**:
- ✅ `folderId` NOT NULL con FK CASCADE
- ✅ `description` agregada
- ✅ Campos visuales (color, icon)
- ❌ SIN multiidioma en datos (solo UI)

### 3. QuestionEntity (MODIFICADA)

```kotlin
@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: String,
    val text: String,                        // Markdown, idioma del usuario
    val explanation: String? = null,         // Markdown, idioma del usuario
    val difficulty: DifficultyLevel = DifficultyLevel.MEDIUM,
    val createdAt: Long = System.currentTimeMillis()
)

enum class DifficultyLevel {
    EASY,
    MEDIUM,
    HARD,
    EXPERT
}
```

**Cambios**:
- ✅ Añadido `explanation` (Markdown)
- ✅ Añadido `difficulty`
- ✅ Timestamp
- ❌ ELIMINADO `correctOptionIds` (va en Option.isCorrect)
- ❌ SIN multiidioma en datos

**Nota**: Question NO tiene FK directo a Pack. La relación es M:N via PackQuestionEntity para permitir reutilización futura.

### 4. OptionEntity (MODIFICADA)

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
    val label: String,                       // "A", "B", "C", "D"
    val text: String,                        // Markdown, idioma del usuario
    val isCorrect: Boolean,                  // MVP: solo 1 con true por question
    val position: Int,                       // Orden para Study mode (0, 1, 2, 3)
    val createdAt: Long = System.currentTimeMillis()
)
```

**Cambios**:
- ✅ Añadido `label` (A/B/C/D para UI)
- ✅ Añadido `isCorrect` (boolean)
- ✅ Añadido `position` (orden fijo en Study)
- ✅ Timestamp
- ❌ SIN multiidioma

**Validación**: Debe haber exactamente 1 option con `isCorrect = true` por question (validar en repositorio).

### 5. PackQuestionEntity (MODIFICADA - CON FKs)

```kotlin
@Entity(
    tableName = "pack_questions",
    primaryKeys = ["packId", "questionId"],
    foreignKeys = [
        ForeignKey(
            entity = PackEntity::class,
            parentColumns = ["id"],
            childColumns = ["packId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("packId"), Index("questionId")]
)
data class PackQuestionEntity(
    val packId: String,
    val questionId: String,
    val sortOrder: Int                       // Orden estable en el pack
)
```

**Cambios Críticos**:
- ✅ Añadidos Foreign Keys (CRÍTICO para integridad)
- ✅ CASCADE DELETE en ambos lados
- ✅ Renombrado `position` a `sortOrder` para claridad

### 6. AttemptEntity (NUEVA)

```kotlin
@Entity(tableName = "attempts")
data class AttemptEntity(
    @PrimaryKey val id: String,
    val mode: AttemptMode,                   // STUDY | GAME
    val startedAt: Long,
    val completedAt: Long? = null,           // null = en progreso
    val durationMs: Long? = null,            // Total al completar
    val score: Int = 0,                      // Solo para Game, 0 para Study
    val primaryPackId: String? = null,       // Para Study (1 pack)
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0
)

enum class AttemptMode {
    STUDY,
    GAME
}
```

**Características**:
- ✅ Metadatos de sesión Study/Game
- ✅ Soporte para attempts en progreso (`completedAt = null`)
- ✅ Stats agregadas (totalQuestions, correctAnswers)
- ✅ `primaryPackId` útil para Study de 1 pack

### 7. AttemptPackEntity (NUEVA)

```kotlin
@Entity(
    tableName = "attempt_packs",
    primaryKeys = ["attemptId", "packId"],
    foreignKeys = [
        ForeignKey(
            entity = AttemptEntity::class,
            parentColumns = ["id"],
            childColumns = ["attemptId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PackEntity::class,
            parentColumns = ["id"],
            childColumns = ["packId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("attemptId"), Index("packId")]
)
data class AttemptPackEntity(
    val attemptId: String,
    val packId: String
)
```

**Uso**:
- Study: 1 registro (o ninguno si se usa primaryPackId)
- Game: N registros (multi-pack)

### 8. AttemptAnswerEntity (NUEVA)

```kotlin
@Entity(
    tableName = "attempt_answers",
    foreignKeys = [
        ForeignKey(
            entity = AttemptEntity::class,
            parentColumns = ["id"],
            childColumns = ["attemptId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = OptionEntity::class,
            parentColumns = ["id"],
            childColumns = ["pickedOptionId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("attemptId"), Index("questionId")]
)
data class AttemptAnswerEntity(
    @PrimaryKey val id: String,
    val attemptId: String,
    val questionId: String,
    val pickedOptionId: String?,            // null = no respondida/timeout
    val isCorrect: Boolean,
    val timeMs: Long,                       // Tiempo tomado en responder
    val timeLimitMs: Long? = null,          // Solo Game (límite dinámico)
    val answeredAt: Long = System.currentTimeMillis()
)
```

**Características**:
- ✅ Trackea cada respuesta individual
- ✅ `timeMs` obligatorio (para algoritmo de timer dinámico)
- ✅ `timeLimitMs` para análisis en Game
- ✅ Soporte para timeouts (`pickedOptionId = null`)

---

## 🗂️ Diagrama Entidad-Relación

### Diagrama Completo con Attempts

```
┌──────────────────────────────────────────────────────────────────────────┐
│                      UQUIZ DATABASE SCHEMA - MVP                          │
└──────────────────────────────────────────────────────────────────────────┘

╔═══════════════════════════════════════════════════════════════════════════╗
║                      PARTE 1: ORGANIZACIÓN DE CONTENIDO                   ║
╚═══════════════════════════════════════════════════════════════════════════╝

┌─────────────────────────────────────────────────────────────────┐
│                          FolderEntity                            │
├──────────────────────────────────────────────────────────────────┤
│ PK  id: String                                                   │
│     name: String                                                 │
│ FK  parentId: String? ───────┐  (self-reference)                │
│     position: Int            │  CASCADE DELETE                   │
│     createdAt: Long          │                                   │
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
│     title: String                                                 │
│     description: String?                                          │
│ FK  folderId: String NOT NULL  ← CASCADE DELETE                   │
│     position: Int                                                 │
│     color: String?                                                │
│     icon: String?                                                 │
│     createdAt: Long                                               │
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
│     sortOrder: Int                                                 │
└────────────────────────────────────────────────────────────────────┘
        │
        │ N:1
        │
        ▼
┌────────────────────────────────────────────────────────────────────┐
│                        QuestionEntity                               │
├─────────────────────────────────────────────────────────────────────┤
│ PK  id: String                                                      │
│     text: String           (Markdown)                               │
│     explanation: String?   (Markdown)                               │
│     difficulty: DifficultyLevel (EASY|MEDIUM|HARD|EXPERT)           │
│     createdAt: Long                                                 │
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
│     label: String       (A/B/C/D)                                    │
│     text: String        (Markdown)                                   │
│     isCorrect: Boolean  (MVP: solo 1 true por question)              │
│     position: Int       (orden para Study mode)                      │
│     createdAt: Long                                                  │
└──────────────────────────────────────────────────────────────────────┘

╔═══════════════════════════════════════════════════════════════════════════╗
║                   PARTE 2: SISTEMA DE ATTEMPTS Y STATS                    ║
╚═══════════════════════════════════════════════════════════════════════════╝

┌─────────────────────────────────────────────────────────────────────┐
│                         AttemptEntity                                │
├──────────────────────────────────────────────────────────────────────┤
│ PK  id: String                                                       │
│     mode: AttemptMode (STUDY | GAME)                                 │
│     startedAt: Long                                                  │
│     completedAt: Long?      (null = en progreso)                     │
│     durationMs: Long?                                                │
│     score: Int              (0 para Study)                           │
│     primaryPackId: String?  (para Study de 1 pack)                   │
│     totalQuestions: Int                                              │
│     correctAnswers: Int                                              │
└──────────────────────────────────────────────────────────────────────┘
        │
        ├─────────────────────────────────────────────────────────┐
        │ 1:N                                                     │ 1:N
        │                                                         │
        ▼                                                         ▼
┌──────────────────────────────┐                  ┌─────────────────────────────┐
│    AttemptPackEntity         │                  │   AttemptAnswerEntity       │
│    (solo para Game)          │                  │                             │
├───────────────────────────────┤                  ├──────────────────────────────┤
│ PK  attemptId: String        │                  │ PK  id: String              │
│ PK  packId: String           │                  │ FK  attemptId: String       │
│ FK  → AttemptEntity CASCADE  │                  │ FK  questionId: String      │
│ FK  → PackEntity CASCADE     │                  │ FK  pickedOptionId: String? │
└──────────────────────────────┘                  │     isCorrect: Boolean      │
                                                   │     timeMs: Long            │
                                                   │     timeLimitMs: Long?      │
                                                   │     answeredAt: Long        │
                                                   └─────────────────────────────┘
```

### Relaciones y Cardinalidad

| Relación | Tipo | Descripción |
|----------|------|-------------|
| **Folder → Folder** | 1:N (self) | Anidamiento infinito de carpetas |
| **Folder → Pack** | 1:N | Un folder contiene múltiples packs |
| **Pack → Question** | M:N | Via PackQuestionEntity (junction table) |
| **Question → Option** | 1:N | Una pregunta tiene múltiples opciones |
| **Attempt → AttemptPack** | 1:N | Packs incluidos en sesión Game |
| **Attempt → AttemptAnswer** | 1:N | Respuestas en la sesión |
| **Question → AttemptAnswer** | 1:N | Historial de respuestas por pregunta |
| **Option → AttemptAnswer** | 1:N | Historial de selecciones de opción |

---

## 📂 Jerarquía de Contenidos

### Estructura de Navegación

```
Folder (raíz)
  ├── Folder (hijo nivel 1)
  │     ├── Pack A
  │     │     ├── Question 1
  │     │     │     ├── Option A (isCorrect: false, position: 0)
  │     │     │     ├── Option B (isCorrect: true, position: 1)
  │     │     │     ├── Option C (isCorrect: false, position: 2)
  │     │     │     └── Option D (isCorrect: false, position: 3)
  │     │     └── Question 2
  │     │           └── Options...
  │     ├── Pack B
  │     └── Folder (hijo nivel 2)
  │           └── Pack C
  └── Folder (hijo nivel 1)
        └── Pack D
```

### Reglas de Negocio

1. **Folder**:
   - ✅ Puede contener: Folders hijos + Packs
   - ✅ NO puede contener Questions directamente
   - ✅ Anidamiento infinito permitido
   - ✅ Al eliminar: CASCADE a todos los hijos (folders + packs)

2. **Pack**:
   - ✅ Siempre debe estar en un Folder (`folderId` NOT NULL)
   - ✅ Contiene Questions (via PackQuestionEntity)
   - ✅ Al eliminar: CASCADE a PackQuestionEntity (las Questions se eliminan si no están en otros packs)

3. **Question**:
   - ✅ Se crea SIEMPRE desde un Pack (no hay "question library")
   - ✅ Puede estar en múltiples Packs (M:N via PackQuestionEntity)
   - ✅ Al eliminar: CASCADE a Options + AttemptAnswers + PackQuestionEntity

4. **Option**:
   - ✅ Exactamente 1 option con `isCorrect = true` por question (validar en repositorio)
   - ✅ `position` define orden en Study mode (0, 1, 2, 3...)
   - ✅ `label` para UI (A, B, C, D...)

---

## 📊 Sistema de Attempts y Stats

### Flujo Study Mode

```kotlin
// 1. Crear attempt
val attempt = AttemptEntity(
    id = UUID.randomUUID().toString(),
    mode = AttemptMode.STUDY,
    startedAt = System.currentTimeMillis(),
    primaryPackId = selectedPackId
)
attemptDao.insert(attempt)

// 2. Por cada pregunta respondida
val answer = AttemptAnswerEntity(
    id = UUID.randomUUID().toString(),
    attemptId = attempt.id,
    questionId = question.id,
    pickedOptionId = selectedOption.id,
    isCorrect = selectedOption.isCorrect,
    timeMs = elapsedTime
)
attemptAnswerDao.insert(answer)

// 3. Al finalizar sesión
val updatedAttempt = attempt.copy(
    completedAt = System.currentTimeMillis(),
    durationMs = totalDuration,
    totalQuestions = questionsCount,
    correctAnswers = correctCount
)
attemptDao.update(updatedAttempt)
```

### Flujo Game Mode

```kotlin
// 1. Crear attempt
val attempt = AttemptEntity(
    id = UUID.randomUUID().toString(),
    mode = AttemptMode.GAME,
    startedAt = System.currentTimeMillis()
)
attemptDao.insert(attempt)

// 2. Insertar packs seleccionados
selectedPacks.forEach { packId ->
    attemptPackDao.insert(
        AttemptPackEntity(
            attemptId = attempt.id,
            packId = packId
        )
    )
}

// 3. Deduplicar questions
val allQuestions = selectedPacks.flatMap { packId ->
    packQuestionDao.getQuestionsInPack(packId)
}.distinctBy { it.id }

// 4. Por cada pregunta con timer
val answer = AttemptAnswerEntity(
    id = UUID.randomUUID().toString(),
    attemptId = attempt.id,
    questionId = question.id,
    pickedOptionId = selectedOption?.id,  // null si timeout
    isCorrect = selectedOption?.isCorrect ?: false,
    timeMs = elapsedTime,
    timeLimitMs = calculatedTimeLimit  // Algoritmo dinámico
)
attemptAnswerDao.insert(answer)

// 5. Al finalizar con score
val updatedAttempt = attempt.copy(
    completedAt = System.currentTimeMillis(),
    durationMs = totalDuration,
    score = calculateScore(answers),
    totalQuestions = questionsCount,
    correctAnswers = correctCount
)
attemptDao.update(updatedAttempt)
```

### Stats Derivadas

```kotlin
// Global stats
@Query("""
    SELECT
        COUNT(DISTINCT id) as totalAttempts,
        SUM(totalQuestions) as totalQuestionsAnswered,
        SUM(correctAnswers) as totalCorrect,
        AVG(CAST(correctAnswers AS FLOAT) / totalQuestions) as avgAccuracy,
        AVG(durationMs) as avgDurationMs
    FROM attempts
    WHERE completedAt IS NOT NULL
""")
fun getGlobalStats(): Flow<GlobalStats>

// Stats por pack
@Query("""
    SELECT
        p.id,
        p.title,
        COUNT(DISTINCT a.id) as attempts,
        AVG(CAST(aa.isCorrect AS INT)) as accuracy,
        AVG(aa.timeMs) as avgTimeMs
    FROM packs p
    LEFT JOIN attempt_packs ap ON p.id = ap.packId
    LEFT JOIN attempts a ON ap.attemptId = a.id
    LEFT JOIN attempt_answers aa ON a.id = aa.attemptId
    WHERE p.id = :packId AND a.completedAt IS NOT NULL
    GROUP BY p.id
""")
fun getPackStats(packId: String): Flow<PackStats>

// Progreso por pack (% preguntas respondidas correctamente al menos 1 vez)
@Query("""
    SELECT
        COUNT(DISTINCT pq.questionId) as totalQuestions,
        COUNT(DISTINCT CASE WHEN aa.isCorrect THEN pq.questionId END) as masteredQuestions
    FROM pack_questions pq
    LEFT JOIN attempt_answers aa ON pq.questionId = aa.questionId
    WHERE pq.packId = :packId
""")
fun getPackProgress(packId: String): Flow<PackProgress>
```

---

## 🎮 Modos de Juego

### Study Mode

**Características**:
- ✅ Selección de 1 pack
- ✅ Preguntas en orden según `sortOrder` en PackQuestionEntity
- ✅ Opciones en orden según `position` en OptionEntity
- ✅ Sin timer
- ✅ Verificación inmediata al responder
- ✅ Panel de explicación (Markdown) tras verificar
- ✅ Navegación: anterior/siguiente
- ✅ Guardar tiempos (para algoritmo dinámico futuro)

**Flujo UI**:
```
1. PackDetailScreen → "Start Study" button
2. StudyScreen:
   - Muestra pregunta (Markdown rendered)
   - Muestra opciones en orden (A, B, C, D)
   - Usuario selecciona → "Verify" button
   - Muestra feedback (correcto/incorrecto)
   - Muestra panel de explicación (Markdown)
   - Botones: "Previous" | "Next"
3. Al finalizar → StudyResultScreen (stats de la sesión)
```

### Game Mode

**Características**:
- ✅ Selección multi-pack (checkboxes)
- ✅ Deduplicación de questions (distinct by questionId)
- ✅ Cola de preguntas aleatoria (shuffle)
- ✅ Opciones en orden aleatorio (shuffle por pregunta)
- ✅ Temporizador decreciente (algoritmo dinámico v1)
- ✅ Sistema de puntuación:
  - Respuesta correcta rápida: +100 pts
  - Respuesta correcta lenta: +50 pts
  - Timeout/incorrecta: 0 pts
- ✅ Resumen final: score, accuracy, tiempo total

**Algoritmo de Timer Dinámico v1**:
```kotlin
fun calculateTimeLimit(question: Question, difficulty: DifficultyLevel): Long {
    // Base por dificultad
    val baseTime = when (difficulty) {
        DifficultyLevel.EASY -> 15_000L    // 15s
        DifficultyLevel.MEDIUM -> 20_000L  // 20s
        DifficultyLevel.HARD -> 25_000L    // 25s
        DifficultyLevel.EXPERT -> 30_000L  // 30s
    }

    // Ajustar por longitud de texto (caracteres de Markdown)
    val textLength = question.text.length + question.options.sumOf { it.text.length }
    val lengthBonus = (textLength / 100) * 1000L  // +1s por cada 100 chars

    return baseTime + lengthBonus
}
```

**Futuro**: Ajustar según historial de respuestas del usuario (timeMs promedio por difficulty).

**Flujo UI**:
```
1. GameSetupScreen → Seleccionar packs (multi-select)
2. GameScreen:
   - Timer visual (circular progress)
   - Pregunta (Markdown)
   - Opciones shuffled (A, B, C, D positions random)
   - Auto-avanzar al seleccionar o timeout
   - Feedback inmediato (✓/✗)
   - Score acumulado en top
3. GameResultScreen:
   - Score final
   - Accuracy
   - Tiempo total
   - Desglose por pregunta
```

---

## 🌍 Estrategia de Multiidioma

### Enfoque Correcto: Solo UI

**IMPORTANTE**: El contenido creado por el usuario (Folders, Packs, Questions, Options) **NO tiene multiidioma**. El usuario crea contenido en su idioma preferido.

**Multiidioma SOLO para**:
- ✅ Interfaz de usuario (botones, labels, mensajes)
- ✅ Strings del sistema (errores, confirmaciones)
- ✅ Navegación y menús

### Estructura de Recursos

```
res/
├── values/strings.xml              # EN (fallback)
├── values-es/strings.xml           # ES
└── values-ca/strings.xml           # CA
```

### Ejemplo strings.xml

```xml
<!-- res/values/strings.xml (EN) -->
<resources>
    <string name="app_name">UQuiz</string>

    <!-- Navigation -->
    <string name="nav_folders">Folders</string>
    <string name="nav_packs">Packs</string>
    <string name="nav_stats">Stats</string>

    <!-- Actions -->
    <string name="action_create_folder">Create Folder</string>
    <string name="action_create_pack">Create Pack</string>
    <string name="action_create_question">Add Question</string>
    <string name="action_start_study">Start Study</string>
    <string name="action_start_game">Start Game</string>
    <string name="action_verify">Verify</string>
    <string name="action_next">Next</string>
    <string name="action_previous">Previous</string>

    <!-- Study Mode -->
    <string name="study_correct">Correct!</string>
    <string name="study_incorrect">Incorrect</string>
    <string name="study_explanation">Explanation</string>
    <string name="study_progress">%1$d / %2$d</string>

    <!-- Game Mode -->
    <string name="game_score">Score: %1$d</string>
    <string name="game_timeout">Time\'s up!</string>
    <string name="game_final_score">Final Score: %1$d</string>

    <!-- Question Editor -->
    <string name="editor_question_text">Question text (Markdown)</string>
    <string name="editor_explanation">Explanation (Markdown)</string>
    <string name="editor_difficulty">Difficulty</string>
    <string name="editor_option_label">Option %1$s</string>
    <string name="editor_mark_correct">Mark as correct</string>
    <string name="editor_preview">Preview</string>

    <!-- Difficulty -->
    <string name="difficulty_easy">Easy</string>
    <string name="difficulty_medium">Medium</string>
    <string name="difficulty_hard">Hard</string>
    <string name="difficulty_expert">Expert</string>

    <!-- Stats -->
    <string name="stats_attempts">Attempts</string>
    <string name="stats_accuracy">Accuracy</string>
    <string name="stats_avg_time">Avg. Time</string>
    <string name="stats_progress">Progress</string>

    <!-- Errors -->
    <string name="error_no_correct_option">At least one option must be marked as correct</string>
    <string name="error_empty_question">Question text cannot be empty</string>
    <string name="error_empty_option">Option text cannot be empty</string>

    <!-- Confirmations -->
    <string name="confirm_delete_folder">Delete this folder and all its contents?</string>
    <string name="confirm_delete_pack">Delete this pack and all its questions?</string>
    <string name="confirm_delete_question">Delete this question?</string>
</resources>
```

```xml
<!-- res/values-es/strings.xml (ES) -->
<resources>
    <string name="app_name">UQuiz</string>

    <string name="nav_folders">Carpetas</string>
    <string name="nav_packs">Paquetes</string>
    <string name="nav_stats">Estadísticas</string>

    <string name="action_create_folder">Crear Carpeta</string>
    <string name="action_create_pack">Crear Paquete</string>
    <string name="action_create_question">Añadir Pregunta</string>
    <string name="action_start_study">Iniciar Estudio</string>
    <string name="action_start_game">Iniciar Juego</string>
    <string name="action_verify">Verificar</string>
    <string name="action_next">Siguiente</string>
    <string name="action_previous">Anterior</string>

    <string name="study_correct">¡Correcto!</string>
    <string name="study_incorrect">Incorrecto</string>
    <string name="study_explanation">Explicación</string>

    <!-- ... más traducciones ... -->
</resources>
```

### Uso en Compose

```kotlin
@Composable
fun StudyScreen() {
    // Strings traducidas automáticamente
    Button(onClick = { /* verify */ }) {
        Text(stringResource(R.string.action_verify))
    }

    // Contenido del usuario (sin traducción)
    Text(text = question.text)  // Ya está en idioma del usuario
}
```

---

## 📝 Soporte de Markdown

### Campos con Markdown

- `QuestionEntity.text`
- `QuestionEntity.explanation`
- `OptionEntity.text`

### Librería Recomendada

**Markwon** (https://github.com/noties/Markwon)

```gradle
// build.gradle.kts
dependencies {
    implementation("io.noties.markwon:core:4.6.2")
    implementation("io.noties.markwon:ext-tables:4.6.2")
    implementation("io.noties.markwon:ext-strikethrough:4.6.2")
    implementation("io.noties.markwon:ext-tasklist:4.6.2")
}
```

### Composable para Markdown

```kotlin
@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    val context = LocalContext.current
    val markwon = remember {
        Markwon.builder(context)
            .usePlugin(TablesPlugin.create(context))
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(TaskListPlugin.create(context))
            .build()
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            TextView(ctx).apply {
                textSize = style.fontSize.value
                setTextColor(style.color.toArgb())
            }
        },
        update = { view ->
            markwon.setMarkdown(view, markdown)
        }
    )
}
```

### Editor con Preview

```kotlin
@Composable
fun QuestionEditorScreen() {
    var questionText by remember { mutableStateOf("") }
    var showPreview by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = questionText,
            onValueChange = { questionText = it },
            label = { Text(stringResource(R.string.editor_question_text)) },
            modifier = Modifier.fillMaxWidth()
        )

        Row {
            TextButton(onClick = { showPreview = !showPreview }) {
                Text(stringResource(R.string.editor_preview))
            }
        }

        if (showPreview) {
            Card {
                MarkdownText(
                    markdown = questionText,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
```

### Validaciones de Markdown

```kotlin
object MarkdownValidator {
    fun isValid(markdown: String): Boolean {
        // Validaciones básicas
        if (markdown.isBlank()) return false

        // Verificar balance de sintaxis
        val backtickCount = markdown.count { it == '`' }
        if (backtickCount % 2 != 0) return false

        // Más validaciones según necesidad
        return true
    }

    fun sanitize(markdown: String): String {
        // Sanitización básica (evitar XSS si se renderiza HTML)
        return markdown
            .replace("<script", "&lt;script")
            .replace("javascript:", "")
    }
}
```

---

## 🔄 Operaciones Principales y Cascadas

### 1. Crear Jerarquía Completa (desde UI)

```kotlin
// Caso de uso: Usuario crea estructura Folder → Pack → Question
suspend fun createCompleteStructure() {
    // 1. Crear Folder
    val folder = FolderEntity(
        id = UUID.randomUUID().toString(),
        name = "Matemáticas",  // Idioma del usuario
        parentId = null,
        position = 0
    )
    folderDao.insert(folder)

    // 2. Crear Pack en ese Folder
    val pack = PackEntity(
        id = UUID.randomUUID().toString(),
        title = "Álgebra Básica",
        description = "Ecuaciones lineales y cuadráticas",
        folderId = folder.id
    )
    packDao.insert(pack)

    // 3. Crear Question (SOLO desde Pack)
    val question = QuestionEntity(
        id = UUID.randomUUID().toString(),
        text = "Resuelve: `2x + 3 = 7`",  // Markdown
        explanation = "Resta 3 a ambos lados: `2x = 4`, luego divide por 2: `x = 2`",
        difficulty = DifficultyLevel.EASY
    )
    questionDao.insert(question)

    // 4. Crear Options
    val options = listOf(
        OptionEntity(
            id = UUID.randomUUID().toString(),
            questionId = question.id,
            label = "A",
            text = "`x = 1`",
            isCorrect = false,
            position = 0
        ),
        OptionEntity(
            id = UUID.randomUUID().toString(),
            questionId = question.id,
            label = "B",
            text = "`x = 2`",
            isCorrect = true,  // ← Solo 1 correcta
            position = 1
        ),
        OptionEntity(
            id = UUID.randomUUID().toString(),
            questionId = question.id,
            label = "C",
            text = "`x = 3`",
            isCorrect = false,
            position = 2
        ),
        OptionEntity(
            id = UUID.randomUUID().toString(),
            questionId = question.id,
            label = "D",
            text = "`x = 5`",
            isCorrect = false,
            position = 3
        )
    )
    optionDao.insertAll(options)

    // 5. Vincular Question al Pack
    packQuestionDao.insert(
        PackQuestionEntity(
            packId = pack.id,
            questionId = question.id,
            sortOrder = 0
        )
    )
}
```

### 2. Eliminar Folder (Cascada Recursiva)

```kotlin
folderDao.delete(folderId)

// Room ejecuta automáticamente:
// 1. DELETE FROM folders WHERE parentId = folderId (folders hijos)
// 2. Para cada folder hijo, repite paso 1 (recursivo CASCADE)
// 3. DELETE FROM packs WHERE folderId = folderId (packs directos)
// 4. DELETE FROM packs WHERE folderId IN (folders_hijos) (packs de hijos)
// 5. DELETE FROM pack_questions WHERE packId IN (packs_eliminados)
// 6. DELETE FROM questions WHERE id IN (questions_sin_otros_packs)
// 7. DELETE FROM options WHERE questionId IN (questions_eliminadas)
// 8. DELETE FROM attempt_answers WHERE questionId IN (questions_eliminadas)
```

**Nota**: Questions solo se eliminan si no están en otros packs (verificar con trigger o lógica en repositorio).

### 3. Eliminar Pack

```kotlin
packDao.delete(packId)

// Cascada:
// 1. DELETE FROM pack_questions WHERE packId = packId
// 2. DELETE FROM attempt_packs WHERE packId = packId
// 3. Si questions quedan huérfanas (no en otros packs):
//    - DELETE FROM questions WHERE id NOT IN (SELECT DISTINCT questionId FROM pack_questions)
//    - DELETE FROM options WHERE questionId IN (questions_huerfanas)
//    - DELETE FROM attempt_answers WHERE questionId IN (questions_huerfanas)
```

**Implementación con Trigger** (opcional):

```sql
CREATE TRIGGER delete_orphan_questions
AFTER DELETE ON pack_questions
BEGIN
    DELETE FROM questions
    WHERE id = OLD.questionId
    AND id NOT IN (SELECT DISTINCT questionId FROM pack_questions);
END;
```

### 4. Eliminar Question

```kotlin
questionDao.delete(questionId)

// Cascada:
// 1. DELETE FROM options WHERE questionId = questionId
// 2. DELETE FROM pack_questions WHERE questionId = questionId
// 3. DELETE FROM attempt_answers WHERE questionId = questionId
```

### 5. Mover Pack entre Folders

```kotlin
suspend fun movePack(packId: String, newFolderId: String) {
    packDao.updateFolderId(packId, newFolderId)
}

@Query("UPDATE packs SET folderId = :newFolderId WHERE id = :packId")
suspend fun updateFolderId(packId: String, newFolderId: String)
```

### 6. Mover Folder (con validación anti-ciclo)

```kotlin
suspend fun moveFolder(folderId: String, newParentId: String?) {
    // VALIDACIÓN CRÍTICA: Prevenir ciclos
    if (newParentId != null && isDescendant(folderId, newParentId)) {
        throw IllegalArgumentException("Cannot move folder to its own descendant")
    }

    folderDao.updateParentId(folderId, newParentId)
}

private suspend fun isDescendant(ancestorId: String, potentialDescendantId: String): Boolean {
    var currentId: String? = potentialDescendantId
    while (currentId != null) {
        if (currentId == ancestorId) return true
        val folder = folderDao.getFolderById(currentId)
        currentId = folder?.parentId
    }
    return false
}
```

### 7. Deduplicar Questions en Game Mode

```kotlin
suspend fun getDeduplicatedQuestions(packIds: List<String>): List<Question> {
    val allQuestions = mutableMapOf<String, Question>()

    packIds.forEach { packId ->
        val questions = questionRepository.getQuestionsInPack(packId)
        questions.forEach { question ->
            // Solo agregar si no existe (deduplica)
            if (!allQuestions.containsKey(question.id)) {
                allQuestions[question.id] = question
            }
        }
    }

    return allQuestions.values.shuffled()  // Aleatorio para Game
}
```

### 8. Queries Recursivas para Folders

```kotlin
// FolderDao.kt

// Obtener jerarquía completa desde un folder
@Query("""
    WITH RECURSIVE folder_tree AS (
        SELECT * FROM folders WHERE id = :rootFolderId
        UNION ALL
        SELECT f.*
        FROM folders f
        INNER JOIN folder_tree ft ON f.parentId = ft.id
    )
    SELECT * FROM folder_tree ORDER BY position
""")
fun getFolderHierarchy(rootFolderId: String): Flow<List<FolderEntity>>

// Obtener path completo (breadcrumbs)
@Query("""
    WITH RECURSIVE folder_path AS (
        SELECT id, name, parentId, 1 as level
        FROM folders WHERE id = :folderId
        UNION ALL
        SELECT f.id, f.name, f.parentId, fp.level + 1
        FROM folders f
        INNER JOIN folder_path fp ON f.id = fp.parentId
    )
    SELECT * FROM folder_path ORDER BY level DESC
""")
fun getFolderPath(folderId: String): Flow<List<FolderEntity>>

// Contenido de un folder (hijos directos + packs)
data class FolderContent(
    val subfolders: List<FolderEntity>,
    val packs: List<PackEntity>
)

@Transaction
suspend fun getFolderContent(folderId: String): FolderContent {
    val subfolders = getChildFolders(folderId)
    val packs = getPacksInFolder(folderId)
    return FolderContent(subfolders, packs)
}

@Query("SELECT * FROM folders WHERE parentId = :folderId ORDER BY position")
suspend fun getChildFolders(folderId: String): List<FolderEntity>

@Query("SELECT * FROM packs WHERE folderId = :folderId ORDER BY position")
suspend fun getPacksInFolder(folderId: String): List<PackEntity>
```

---

## 📐 Contratos de Repositorios

### Interfaces Domain Layer

```kotlin
// domain/repository/FolderRepository.kt
interface FolderRepository {
    fun observeRootFolders(): Flow<List<Folder>>
    fun observeFolderContent(folderId: String): Flow<FolderContent>
    fun observeFolderPath(folderId: String): Flow<List<Folder>>
    suspend fun createFolder(name: String, parentId: String?): Folder
    suspend fun updateFolder(folder: Folder)
    suspend fun deleteFolder(folderId: String)
    suspend fun moveFolder(folderId: String, newParentId: String?)
}

// domain/repository/PackRepository.kt
interface PackRepository {
    fun observePacksInFolder(folderId: String): Flow<List<Pack>>
    fun observePack(packId: String): Flow<Pack?>
    suspend fun createPack(pack: Pack): Pack
    suspend fun updatePack(pack: Pack)
    suspend fun deletePack(packId: String)
    suspend fun movePack(packId: String, newFolderId: String)
}

// domain/repository/QuestionRepository.kt
interface QuestionRepository {
    fun observeQuestionsInPack(packId: String): Flow<List<Question>>
    fun observeQuestion(questionId: String): Flow<Question?>
    suspend fun createQuestion(question: Question, packId: String): Question
    suspend fun updateQuestion(question: Question)
    suspend fun deleteQuestion(questionId: String)
    suspend fun validateOptions(options: List<Option>): Boolean
}

// domain/repository/StudyRepository.kt
interface StudyRepository {
    suspend fun startStudyAttempt(packId: String): Attempt
    suspend fun recordAnswer(attemptId: String, answer: Answer): Answer
    suspend fun finishAttempt(attemptId: String): Attempt
    fun observeAttempt(attemptId: String): Flow<Attempt?>
    fun observeAttemptProgress(attemptId: String): Flow<AttemptProgress>
}

// domain/repository/GameRepository.kt
interface GameRepository {
    suspend fun startGameAttempt(packIds: List<String>): GameSession
    suspend fun getNextQuestion(sessionId: String): Question?
    suspend fun submitAnswer(sessionId: String, answer: Answer, timeMs: Long): AnswerResult
    suspend fun finishGameAttempt(sessionId: String): GameResult
    fun observeGameSession(sessionId: String): Flow<GameSession?>
}

// domain/repository/StatsRepository.kt
interface StatsRepository {
    fun observeGlobalStats(): Flow<GlobalStats>
    fun observePackStats(packId: String): Flow<PackStats>
    fun observeRecentAttempts(limit: Int): Flow<List<Attempt>>
    fun observeQuestionHistory(questionId: String): Flow<List<AttemptAnswer>>
}

// domain/repository/ImportExportRepository.kt
interface ImportExportRepository {
    suspend fun exportPack(packId: String): String  // JSON
    suspend fun importPack(json: String, folderId: String): Pack
    suspend fun validateImport(json: String): ImportValidation
}
```

### Modelos de Dominio

```kotlin
// domain/model/Folder.kt
data class Folder(
    val id: String,
    val name: String,
    val parentId: String?,
    val position: Int,
    val createdAt: Long
)

// domain/model/Pack.kt
data class Pack(
    val id: String,
    val title: String,
    val description: String?,
    val folderId: String,
    val position: Int,
    val color: String?,
    val icon: String?,
    val createdAt: Long,
    val questionCount: Int = 0  // Calculado
)

// domain/model/Question.kt
data class Question(
    val id: String,
    val text: String,           // Markdown
    val explanation: String?,   // Markdown
    val difficulty: DifficultyLevel,
    val options: List<Option>,
    val createdAt: Long
)

// domain/model/Option.kt
data class Option(
    val id: String,
    val label: String,          // A, B, C, D
    val text: String,           // Markdown
    val isCorrect: Boolean,
    val position: Int
)

// domain/model/Attempt.kt
data class Attempt(
    val id: String,
    val mode: AttemptMode,
    val startedAt: Long,
    val completedAt: Long?,
    val durationMs: Long?,
    val score: Int,
    val totalQuestions: Int,
    val correctAnswers: Int
)

// domain/model/Answer.kt
data class Answer(
    val questionId: String,
    val pickedOptionId: String?,
    val timeMs: Long
)

// domain/model/GameSession.kt
data class GameSession(
    val attemptId: String,
    val questionQueue: List<Question>,
    val currentIndex: Int,
    val score: Int,
    val answers: List<AnswerResult>
)

// domain/model/Stats.kt
data class GlobalStats(
    val totalAttempts: Int,
    val totalQuestionsAnswered: Int,
    val avgAccuracy: Float,
    val avgDurationMs: Long
)

data class PackStats(
    val packId: String,
    val attempts: Int,
    val accuracy: Float,
    val avgTimeMs: Long,
    val progress: Float  // 0.0 - 1.0
)
```

---

## 🔧 Cambios Necesarios

### Resumen de Cambios por Componente

| Componente | Estado Actual | Cambios Necesarios |
|------------|---------------|-------------------|
| **FolderEntity** | ❌ No existe | ✅ Crear completa con self-reference |
| **PackEntity** | ⚠️ Incompleta | ✅ Agregar folderId, description, color, icon, timestamps |
| **QuestionEntity** | ⚠️ Incompleta | ✅ Agregar explanation, difficulty, timestamps; ELIMINAR correctOptionIds |
| **OptionEntity** | ⚠️ Incompleta | ✅ Agregar label, isCorrect, position, timestamp |
| **PackQuestionEntity** | 🚨 Sin FKs | ✅ Agregar Foreign Keys CASCADE; renombrar position → sortOrder |
| **AttemptEntity** | ❌ No existe | ✅ Crear completa |
| **AttemptPackEntity** | ❌ No existe | ✅ Crear completa |
| **AttemptAnswerEntity** | ❌ No existe | ✅ Crear completa |
| **UQuizDatabase** | ⚠️ Versión 1, 4 entidades | ✅ Versión 2, 8 entidades, migración |
| **Converters** | ✅ List<String> | ✅ Agregar DifficultyLevel, AttemptMode |
| **FolderDao** | ❌ No existe | ✅ Crear completo con queries recursivas |
| **PackDao** | ⚠️ Básico | ✅ Agregar queries de folder, stats |
| **QuestionDao** | ⚠️ Básico | ✅ Agregar validaciones, queries por difficulty |
| **AttemptDao** | ❌ No existe | ✅ Crear completo |
| **AttemptPackDao** | ❌ No existe | ✅ Crear completo |
| **AttemptAnswerDao** | ❌ No existe | ✅ Crear completo |
| **Mappers** | ❌ Vacío | ✅ Crear 8 mappers (Entity ↔ Domain) |
| **Repositorios** | ❌ Solo interfaces | ✅ Implementar 7 repositorios |
| **Domain Models** | ⚠️ Básicos | ✅ Crear/actualizar todos (Folder, Attempt, Stats, etc.) |
| **ViewModels** | ❌ No existen | ✅ Crear 8 VMs (Folder, Pack, Study, Game, Stats, Editor, etc.) |
| **UI Composables** | ❌ Solo Greeting | ✅ Crear 10+ pantallas |
| **Navigation** | ❌ Vacío | ✅ Configurar NavGraph completo |
| **DI (Hilt)** | ❌ Vacío | ✅ Configurar módulos |
| **Strings.xml** | ⚠️ Solo app_name | ✅ Agregar 50+ strings en EN/ES/CA |
| **Markdown Support** | ❌ No existe | ✅ Integrar Markwon + previews |
| **Import/Export** | ❌ No existe | ✅ JSON serialization + validation |

---

## 📋 Plan de Implementación

### Sprint 1: Base de Datos Core (5-7 días) 🔴 PRIORITARIO

**Objetivo**: Fundación sólida de datos

- [ ] Crear enum `DifficultyLevel` y `AttemptMode`
- [ ] Actualizar `Converters.kt` (DifficultyLevel, AttemptMode)
- [ ] Crear `FolderEntity` completa
- [ ] Modificar `PackEntity` (agregar folderId, description, color, icon, timestamps)
- [ ] Modificar `QuestionEntity` (agregar explanation, difficulty, timestamps; ELIMINAR correctOptionIds)
- [ ] Modificar `OptionEntity` (agregar label, isCorrect, position, timestamp)
- [ ] Modificar `PackQuestionEntity` (agregar FKs CASCADE, renombrar position → sortOrder)
- [ ] Crear `AttemptEntity`
- [ ] Crear `AttemptPackEntity`
- [ ] Crear `AttemptAnswerEntity`
- [ ] Crear migración `MIGRATION_1_2` completa
- [ ] Actualizar `UQuizDatabase` (versión 2, 8 entidades)
- [ ] **Tests**: DAOs básicos con datos de prueba

**Entregables**: BD funcional con 8 entidades, migraciones tested

### Sprint 2: DAOs Completos (4-5 días)

**Objetivo**: Operaciones CRUD + queries complejas

- [ ] Crear `FolderDao` (CRUD + queries recursivas + path)
- [ ] Actualizar `PackDao` (CRUD + folder queries + stats)
- [ ] Actualizar `QuestionDao` (CRUD + validaciones + queries)
- [ ] Crear `AttemptDao` (CRUD + stats queries)
- [ ] Crear `AttemptPackDao` (CRUD + join queries)
- [ ] Crear `AttemptAnswerDao` (CRUD + analytics queries)
- [ ] Implementar triggers para orphan questions (opcional)
- [ ] **Tests**: Queries complejas, cascadas, validaciones

**Entregables**: DAOs completos con >80% coverage

### Sprint 3: Domain Layer (3-4 días)

**Objetivo**: Modelos y contratos limpios

- [ ] Crear modelos de dominio:
  - `Folder`, `Pack`, `Question`, `Option`
  - `Attempt`, `Answer`, `AttemptProgress`
  - `GameSession`, `GameResult`, `AnswerResult`
  - `GlobalStats`, `PackStats`, `QuestionHistory`
- [ ] Crear interfaces de repositorios (7 repositorios)
- [ ] Crear modelos de validación/error
- [ ] **Tests**: Validaciones de modelos

**Entregables**: Domain layer completo, type-safe

### Sprint 4: Mappers (2-3 días)

**Objetivo**: Conversión Entity ↔ Domain

- [ ] `FolderEntityMapper`
- [ ] `PackEntityMapper`
- [ ] `QuestionEntityMapper`
- [ ] `OptionEntityMapper`
- [ ] `AttemptEntityMapper`
- [ ] `AttemptAnswerEntityMapper`
- [ ] Mappers para agregaciones (QuestionWithOptions, PackWithQuestions, etc.)
- [ ] **Tests**: Mappers bidireccionales

**Entregables**: Mappers tested con edge cases

### Sprint 5: Repositorios (5-6 días)

**Objetivo**: Implementaciones con lógica de negocio

- [ ] `FolderRepositoryImpl` (anti-ciclos, validaciones)
- [ ] `PackRepositoryImpl` (stats agregadas)
- [ ] `QuestionRepositoryImpl` (validación 1 correcta)
- [ ] `StudyRepositoryImpl` (lógica de sesión)
- [ ] `GameRepositoryImpl` (deduplicación, timer, score)
- [ ] `StatsRepositoryImpl` (agregaciones complejas)
- [ ] `ImportExportRepositoryImpl` (JSON serialization)
- [ ] **Tests**: Casos de uso end-to-end

**Entregables**: Repositorios completos, tested

### Sprint 6: Dependency Injection (2 días)

**Objetivo**: Hilt configurado

- [ ] Agregar Hilt dependencies en build.gradle
- [ ] Crear `UQuizApplication` con `@HiltAndroidApp`
- [ ] `DatabaseModule` (provee Database, DAOs)
- [ ] `RepositoryModule` (provee Repositories)
- [ ] `AppModule` (provee Dispatchers, Markwon, etc.)
- [ ] **Tests**: Verificar grafo de dependencias

**Entregables**: DI funcional, app compila

### Sprint 7: ViewModels Core (4-5 días)

**Objetivo**: State management para UI

- [ ] `FolderListViewModel` (navegación de folders)
- [ ] `FolderDetailViewModel` (contenido de folder)
- [ ] `PackDetailViewModel` (lista de questions)
- [ ] `QuestionEditorViewModel` (crear/editar questions)
- [ ] `StudyViewModel` (sesión Study)
- [ ] `GameSetupViewModel` (selección multi-pack)
- [ ] `GameViewModel` (sesión Game con timer)
- [ ] `StatsViewModel` (global y por pack)
- [ ] **Tests**: ViewModels con fake repos

**Entregables**: ViewModels tested, UI states definidos

### Sprint 8: UI - Navigation (2-3 días)

**Objetivo**: Navegación entre pantallas

- [ ] Configurar Navigation Compose
- [ ] Definir rutas (`Screen` sealed class)
- [ ] `NavGraph` completo con argumentos
- [ ] Deep links (opcional)
- [ ] Bottom navigation / drawer
- [ ] **Tests**: Navigation flows

**Entregables**: Navegación funcional

### Sprint 9: UI - CRUD Screens (5-6 días)

**Objetivo**: Pantallas de contenido

- [ ] `FolderListScreen` (árbol de folders)
- [ ] `FolderDetailScreen` (subfolders + packs)
- [ ] `PackDetailScreen` (lista de questions)
- [ ] `QuestionEditorScreen` (Markdown editor + preview)
- [ ] `OptionEditorRow` (composable reutilizable)
- [ ] Dialogs (crear folder, crear pack, eliminar)
- [ ] **Tests**: UI tests básicos

**Entregables**: CRUD funcional, UX básica

### Sprint 10: UI - Study Mode (3-4 días)

**Objetivo**: Modo estudio completo

- [ ] `StudyScreen` (question + options + verify)
- [ ] `ExplanationPanel` (Markdown rendering)
- [ ] Navegación anterior/siguiente
- [ ] Progreso visual
- [ ] `StudyResultScreen` (resumen de sesión)
- [ ] **Tests**: Flujo Study E2E

**Entregables**: Study mode funcional

### Sprint 11: UI - Game Mode (4-5 días)

**Objetivo**: Modo arcade completo

- [ ] `GameSetupScreen` (multi-select packs)
- [ ] `GameScreen` (timer + question + shuffle)
- [ ] Timer visual (CircularProgressIndicator)
- [ ] Sistema de puntuación
- [ ] `GameResultScreen` (score + desglose)
- [ ] **Tests**: Flujo Game E2E

**Entregables**: Game mode funcional

### Sprint 12: UI - Stats (2-3 días)

**Objetivo**: Estadísticas visuales

- [ ] `StatsScreen` (tabs: global/packs)
- [ ] Gráficos básicos (bars, lines) con Canvas/Charts
- [ ] `AttemptHistoryList`
- [ ] Filtros por fecha, modo, pack
- [ ] **Tests**: Stats rendering

**Entregables**: Stats visuales

### Sprint 13: Markdown Support (2-3 días)

**Objetivo**: Renderizado y edición Markdown

- [ ] Integrar Markwon
- [ ] `MarkdownText` composable
- [ ] `MarkdownEditor` con toolbar
- [ ] Preview side-by-side
- [ ] Validación de sintaxis
- [ ] **Tests**: Rendering edge cases

**Entregables**: Markdown funcional

### Sprint 14: Import/Export (3-4 días)

**Objetivo**: JSON import/export

- [ ] Definir esquema JSON
- [ ] Serialización con kotlinx.serialization
- [ ] Validación de imports (schema, duplicados)
- [ ] UI para export (share intent)
- [ ] UI para import (file picker)
- [ ] **Tests**: Import/export con casos edge

**Entregables**: Import/export funcional

### Sprint 15: Internacionalización (2 días)

**Objetivo**: Traducciones completas

- [ ] Crear `res/values-es/strings.xml`
- [ ] Crear `res/values-ca/strings.xml`
- [ ] Migrar todos los strings hardcodeados
- [ ] Verificar contextos (plurals, formatStrings)
- [ ] **Tests**: Cambio de idioma en runtime

**Entregables**: App trilingüe (EN/ES/CA)

### Sprint 16: Testing & Polish (5-6 días)

**Objetivo**: Calidad y estabilidad

- [ ] Tests E2E de flujos principales
- [ ] UI tests con Compose Test
- [ ] Corregir bugs encontrados
- [ ] Optimizaciones de rendimiento (LazyColumn, remember, etc.)
- [ ] Accessibility (content descriptions, semantics)
- [ ] Error handling exhaustivo
- [ ] Loading states
- [ ] **Demo**: Preparar datos de ejemplo

**Entregables**: MVP estable

### Sprint 17: Documentación (2 días)

**Objetivo**: Documentar para TFG

- [ ] README.md completo
- [ ] Arquitectura diagrams (PlantUML)
- [ ] API docs (KDoc)
- [ ] User manual básico
- [ ] Screenshots para TFG

**Entregables**: Documentación completa

---

**Tiempo Total Estimado**: 55-70 días (11-14 semanas / 2.5-3.5 meses)

**Nota**: Tiempo asume desarrollo a tiempo completo. Ajustar según disponibilidad.

---

## 🎯 Alcance MVP

### ✅ Incluido en MVP

#### CRUD de Contenido
- Folders anidables (2+ niveles)
- Packs con title/description/color
- Questions con Markdown (text + explanation)
- Options con labels (A/B/C/D), 1 correcta
- Crear questions SOLO desde Pack

#### Modos de Juego
- Study: 1 pack, sin timer, verificación inmediata, explicación
- Game: multi-pack, timer dinámico, puntuación, deduplicación

#### Stats
- Panel global: attempts, accuracy, avg time
- Stats por pack: attempts, accuracy, progress
- Historial de attempts recientes

#### Funcionalidades Extra
- Import/Export JSON de packs
- Markdown rendering y preview
- Multiidioma UI (EN/ES/CA)
- Validaciones (1 correcta, anti-ciclos, etc.)

### ❌ Fuera del MVP

#### Backend/Cloud
- Cuentas de usuario
- Login/registro
- Sincronización multi-dispositivo
- Backup en cloud

#### Social/Comunidad
- Sharing de packs
- Leaderboards/rankings
- Comentarios/ratings
- Comunidad de packs públicos

#### IA/Avanzado
- Generación de preguntas con IA
- Análisis avanzado de progreso
- Recomendaciones personalizadas
- Adaptive learning

#### Gamificación
- Badges/achievements
- Niveles/XP
- Streaks
- Challenges

#### Otros
- Multi-respuesta (más de 1 correcta)
- Tipos de pregunta (true/false, fill-in, etc.)
- Imágenes en preguntas
- Audio/video
- Modo offline avanzado (sync conflict resolution)

---

## 📊 Métricas del Proyecto

### Base de Datos
- **Entidades**: 8 (5 contenido + 3 attempts)
- **DAOs**: 6
- **Relaciones**: 8 (con FKs CASCADE)
- **Índices**: ~15
- **Migraciones**: 1 (v1→v2)
- **Triggers**: 1-2 (orphan cleanup)

### Repositorios
- **Interfaces**: 7
- **Implementaciones**: 7
- **Use Cases**: ~25

### UI
- **Pantallas**: 12-15
- **ViewModels**: 8
- **Composables reutilizables**: ~30

### Testing
- **Unit tests**: ~100
- **Integration tests**: ~30
- **UI tests**: ~20
- **Coverage objetivo**: >80%

### Código Estimado
- **Entidades + DAOs**: ~1200 líneas
- **Mappers**: ~600 líneas
- **Repositorios**: ~1500 líneas
- **ViewModels**: ~1200 líneas
- **UI Composables**: ~3000 líneas
- **Tests**: ~2000 líneas
- **Strings.xml**: ~500 líneas (x3 idiomas)
- **Total**: ~10,000 líneas de código

### Strings i18n
- **EN**: ~150 strings
- **ES**: ~150 strings
- **CA**: ~150 strings

---

## 🔍 Validación de Viabilidad

### ✅ Fortalezas del Diseño

1. **Arquitectura Sólida**: MVVM + Repository + Room es battle-tested
2. **Separación de Concerns**: Domain layer independiente de framework
3. **Cascadas Correctas**: Integridad referencial con FKs CASCADE
4. **Offline-First**: Room + Flow = reactive local-first
5. **Extensible**: Fácil agregar features post-MVP (multi-respuesta, imágenes, etc.)
6. **Testeable**: Dependency injection + interfaces = easy mocking
7. **Performance**: Índices correctos, queries optimizadas
8. **UX**: Study/Game modes claros, feedback inmediato

### ⚠️ Riesgos y Mitigaciones

| Riesgo | Impacto | Mitigación |
|--------|---------|------------|
| **Cascadas incorrectas** | Alto | Tests exhaustivos de eliminación, triggers para orphans |
| **Ciclos en folders** | Medio | Validación en moveFolder, tests de edge cases |
| **Performance queries recursivas** | Medio | Límite de profundidad (ej. 10 niveles), índices en parentId |
| **Markdown XSS** | Bajo | Sanitización básica, Markwon maneja rendering seguro |
| **Timer drift en Game** | Bajo | Usar SystemClock.elapsedRealtime(), tests con fake clocks |
| **Deduplicación lenta** | Bajo | Optimizar con queries SQL (DISTINCT), no en memoria |
| **Import malicioso** | Medio | Validación estricta de JSON schema, límites de tamaño |

### 🎯 Decisiones de Diseño Clave

1. **¿Por qué M:N en PackQuestion en vez de FK directo?**
   - Permite reutilización futura de questions
   - Más flexible para import/merge
   - Overhead mínimo

2. **¿Por qué NO multiidioma en datos de usuario?**
   - Simplifica enormemente el MVP
   - Usuario crea contenido en SU idioma
   - Multiidioma solo UI es suficiente para v1

3. **¿Por qué 1 sola correcta en MVP?**
   - Simplifica validación y scoring
   - Cubre 80% de casos de uso
   - Fácil extender a multi-respuesta post-MVP

4. **¿Por qué Markdown en vez de rich text?**
   - Portable, versionable (Git)
   - Import/export trivial (JSON)
   - Preview simple con Markwon
   - Familiar para usuarios técnicos

5. **¿Por qué timer dinámico y no fijo?**
   - Más justo (preguntas largas = más tiempo)
   - Mejora engagement
   - Algoritmo v1 simple, v2 puede ser ML-based

---

## 🚀 Próximos Pasos Inmediatos

### Fase 0: Aprobación (ahora)

1. ✅ Revisar este documento completo
2. ❓ Decisiones pendientes:
   - ¿Aprobar diseño de 8 entidades?
   - ¿Confirmar 1 sola correcta en MVP?
   - ¿OK con Markdown (sin imágenes en MVP)?
   - ¿Algún cambio en modos Study/Game?

### Fase 1: Setup (día 1)

1. Crear branch `feature/mvp-database`
2. Agregar dependencias (Hilt, Markwon)
3. Empezar **Sprint 1** (Base de Datos)

### Fase 2: Desarrollo (días 2-70)

1. Seguir plan de sprints
2. Demo cada 2 semanas
3. Ajustar según feedback

### Fase 3: Testing (últimos 5 días)

1. QA exhaustivo
2. Fix bugs críticos
3. Preparar demo final

---

## 📚 Referencias Técnicas

- [Room Database](https://developer.android.com/training/data-storage/room) - Oficial Android
- [Foreign Keys en SQLite](https://www.sqlite.org/foreignkeys.html) - Cascadas
- [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) - NavGraph
- [Hilt DI](https://developer.android.com/training/dependency-injection/hilt-android) - Dependency Injection
- [Markwon](https://github.com/noties/Markwon) - Markdown rendering
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) - JSON
- [MVVM Architecture](https://developer.android.com/topic/architecture) - Best practices
- [UDF in Compose](https://developer.android.com/jetpack/compose/architecture#udf) - State management

---

**Documento generado**: 2026-01-25
**Versión**: 2.0 (diseño MVP corregido)
**Estado del proyecto**: Fase de diseño - Pendiente aprobación
**Autor**: Análisis basado en requisitos de UQuiz TFG MVP
