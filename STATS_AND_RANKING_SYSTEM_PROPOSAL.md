# Sistema de Estadísticas y Rankings - Propuestas de Diseño

**Fecha**: 2026-01-28
**Estado**: Propuesta pendiente de implementación
**Versión**: 1.0

---

## Tabla de Contenidos

1. [Análisis de la Estructura Actual](#análisis-de-la-estructura-actual)
2. [OrderedQuestionWithOptions - ¿Es Necesario?](#orderedquestionwithoptions---es-necesario)
3. [Evaluación de la Arquitectura de Archivos](#evaluación-de-la-arquitectura-de-archivos)
4. [Sistema de Rankings y Categorías](#sistema-de-rankings-y-categorías)
5. [Entidades Faltantes](#entidades-faltantes)
6. [Algoritmos Propuestos](#algoritmos-propuestos)
7. [Ejemplos de Implementación](#ejemplos-de-implementación)
8. [Próximos Pasos](#próximos-pasos)

---

## Análisis de la Estructura Actual

### Estado del Proyecto

El proyecto UQuiz actualmente cuenta con:

- ✅ **8 Entidades principales**: Folder, Pack, Question, Option, PackQuestion, Attempt, AttemptPack, AttemptAnswer
- ✅ **Arquitectura Clean**: Separación en capas (data, domain, ui)
- ✅ **Captura de datos básicos**: Tiempos de respuesta, aciertos/fallos
- ⚠️ **Sin agregación de estadísticas**: No hay entidades para stats calculadas
- ⚠️ **Sin sistema de rankings**: No hay categorías de usuario implementadas

### Estructura de Directorios Actual

```
com.uquiz.android/
├── core/
│   ├── database/
│   │   └── DatabaseModule.kt
│   └── di/
├── data/
│   ├── local/
│   │   ├── dao/           (8 DAOs)
│   │   ├── db/            (Database, Converters, Migrations)
│   │   ├── entity/        (9 entidades)
│   │   ├── enums/         (AttemptMode, DifficultyLevel)
│   │   ├── mapper/        (7 mappers)
│   │   └── relations/     (8 relaciones)
│   └── repository/        (4 implementaciones)
├── domain/
│   ├── model/             (7 modelos)
│   └── repository/        (4 interfaces)
└── ui/
    ├── navigation/
    └── theme/
```

---

## OrderedQuestionWithOptions - ¿Es Necesario?

### Pregunta Original

¿Se puede prescindir del archivo `OrderedQuestionWithOptions` dado que ya existe `QuestionWithOptions`?

### Respuesta: **NO, sigue siendo necesario**

### Análisis Comparativo

#### `QuestionWithOptions`
```kotlin
// app/src/main/java/com/uquiz/android/data/local/relations/QuestionWithOptions.kt
data class QuestionWithOptions(
    @Embedded
    val question: QuestionEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "questionId"
    )
    val options: List<OptionEntity>
)
```

**Propósito**: Representa una pregunta con sus opciones, sin información de contexto o orden.

#### `OrderedQuestionWithOptions`
```kotlin
// app/src/main/java/com/uquiz/android/data/local/relations/OrderedQuestionWithOptions.kt
data class OrderedQuestionWithOptions(
    val position: Int,  // ⬅️ CAMPO ADICIONAL
    @Embedded
    val question: QuestionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "questionId"
    )
    val options: List<OptionEntity>
)
```

**Propósito**: Representa una pregunta con sus opciones **más su posición dentro de un pack**.

### Casos de Uso Actuales

#### En `PackDao.kt`
```kotlin
@Transaction
@Query("""
    SELECT pq.sortOrder as position, q.*
    FROM pack_questions pq
    JOIN questions q ON q.id = pq.questionId
    WHERE pq.packId = :packId
    ORDER BY pq.sortOrder ASC
""")
fun observeOrderedQuestionWithOptions(packId: String): Flow<List<OrderedQuestionWithOptions>>
```

Este query es **esencial** para el modo Study donde las preguntas deben mostrarse en un orden específico definido por `sortOrder` en la tabla `pack_questions`.

#### En `QuestionDao.kt`
```kotlin
@Query("SELECT 0 as position, q.* FROM questions q WHERE q.id = :id")
fun observeQuestionWithOptions(id: String): Flow<List<OrderedQuestionWithOptions>>
```

Usa `0` como posición por defecto para consultas individuales.

### Conclusión

**Ambas estructuras son complementarias, no duplicadas:**

- **`QuestionWithOptions`**: Para consultas simples de pregunta+opciones sin contexto de orden
- **`OrderedQuestionWithOptions`**: Para consultas donde el orden es relevante (dentro de un pack)

**Recomendación**: **Mantener ambas estructuras**

---

## Evaluación de la Arquitectura de Archivos

### Pregunta Original

¿El ordenamiento actual de archivos sigue las mejores prácticas de Android?

### Respuesta: **SÍ, la estructura es excelente**

### Análisis de la Arquitectura

La estructura actual implementa **Clean Architecture + MVVM**, que es el estándar recomendado por Google para aplicaciones Android modernas.

#### Características Positivas

1. **Separación en Capas Clara**
   - `data/`: Implementación de acceso a datos
   - `domain/`: Lógica de negocio pura (independiente de Android)
   - `ui/`: Presentación y navegación

2. **Data Layer Bien Organizada**
   ```
   data/local/
   ├── dao/        # Data Access Objects (Room)
   ├── db/         # Configuración de base de datos
   ├── entity/     # Entidades de Room (SQL)
   ├── enums/      # Enumeraciones del dominio
   ├── mapper/     # Conversión Entity ↔ Domain
   └── relations/  # Relaciones de Room (@Relation)
   ```

3. **Domain Layer Independiente**
   - Modelos de negocio sin dependencias de Android
   - Interfaces de repositorios (contratos)

4. **Principios SOLID Aplicados**
   - **S**ingle Responsibility: Cada clase tiene un propósito único
   - **D**ependency Inversion: Domain no depende de Data, usa interfaces

### Comparación con Proyectos de Referencia de Google

Esta estructura es **idéntica** a las muestras oficiales de Android:
- [Now in Android](https://github.com/android/nowinandroid)
- [Architecture Samples](https://github.com/android/architecture-samples)

### Conclusión

**Recomendación**: **Mantener la estructura actual sin cambios**

---

## Sistema de Rankings y Categorías

### Concepto del Sistema

El usuario debe progresar a través de categorías basadas en su rendimiento:

```
U-Initiate → U-Neophyte → U-Acolyte → U-Disciple →
U-Adept → U-Virtuoso → U-Archon → U-Paragon
```

### Factores que Determinan el Rango

#### En Modo GAME (medido con precisión)
- ✅ Tiempo de reacción por pregunta
- ✅ Aciertos vs fallos
- ✅ Dificultad de la pregunta
- ✅ Racha de respuestas correctas
- ✅ Preguntas respondidas dentro del límite de tiempo

#### En Modo STUDY (medido parcialmente)
- ✅ Cantidad de veces que estudió un pack
- ✅ Cantidad de veces que vio una pregunta
- ✅ Aciertos vs fallos (sin tiempo)
- ❌ No se mide tiempo de reacción (no hay timer)

### Propósito de las Estadísticas

1. **Algoritmo dinámico de tiempo**: Ajustar el tiempo límite según el historial del usuario
2. **Progresión de categorías**: Subir/bajar según rendimiento
3. **Retroalimentación al usuario**: Mostrar progreso y áreas de mejora
4. **Personalización**: Adaptar la dificultad a cada usuario

---

## Entidades Faltantes

### Resumen de Necesidades

Para implementar el sistema de rankings y estadísticas personalizadas, se requieren **3 nuevas entidades**:

1. **UserRankEntity**: Estado de la categoría del usuario
2. **QuestionStatsEntity**: Estadísticas agregadas por pregunta
3. **PackStatsEntity**: Estadísticas agregadas por pack

---

### 1. UserRankEntity

**Propósito**: Almacenar el rango actual del usuario y su progreso.

```kotlin
package com.uquiz.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_rank")
data class UserRankEntity(
    @PrimaryKey
    val id: String = "user_singleton", // Solo habrá 1 registro (usuario único)

    val currentRank: UserRank,
    val rankPoints: Int,        // Puntos en el rango actual
    val totalPoints: Int,       // Puntos totales históricos
    val lifetimeCorrect: Int,   // Total de respuestas correctas
    val lifetimeIncorrect: Int, // Total de respuestas incorrectas

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
```

### Enum de Rangos

```kotlin
package com.uquiz.android.data.local.enums

enum class UserRank(
    val displayName: String,
    val minPoints: Int,
    val maxPoints: Int?
) {
    U_INITIATE("U-Initiate", 0, 99),
    U_NEOPHYTE("U-Neophyte", 100, 299),
    U_ACOLYTE("U-Acolyte", 300, 599),
    U_DISCIPLE("U-Disciple", 600, 999),
    U_ADEPT("U-Adept", 1000, 1499),
    U_VIRTUOSO("U-Virtuoso", 1500, 2199),
    U_ARCHON("U-Archon", 2200, 2999),
    U_PARAGON("U-Paragon", 3000, null); // Sin límite superior

    companion object {
        fun fromPoints(points: Int): UserRank {
            return values().lastOrNull { it.minPoints <= points }
                ?: U_INITIATE
        }
    }

    fun getProgress(currentPoints: Int): Float {
        if (maxPoints == null) return 1.0f // U-Paragon siempre al 100%
        val pointsInRank = currentPoints - minPoints
        val totalPointsInRank = maxPoints - minPoints
        return (pointsInRank.toFloat() / totalPointsInRank).coerceIn(0f, 1f)
    }
}
```

### DAO de UserRank

```kotlin
package com.uquiz.android.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.local.entity.UserRankEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRankDao {

    @Query("SELECT * FROM user_rank WHERE id = 'user_singleton' LIMIT 1")
    suspend fun get(): UserRankEntity?

    @Query("SELECT * FROM user_rank WHERE id = 'user_singleton' LIMIT 1")
    fun observe(): Flow<UserRankEntity?>

    @Upsert
    suspend fun upsert(userRank: UserRankEntity)

    /**
     * Incrementar puntos y actualizar rango si es necesario
     */
    @Query("""
        UPDATE user_rank
        SET totalPoints = totalPoints + :points,
            rankPoints = rankPoints + :points,
            updatedAt = :timestamp
        WHERE id = 'user_singleton'
    """)
    suspend fun addPoints(points: Int, timestamp: Long = System.currentTimeMillis())

    /**
     * Incrementar contadores de aciertos/fallos
     */
    @Query("""
        UPDATE user_rank
        SET lifetimeCorrect = lifetimeCorrect + :correct,
            lifetimeIncorrect = lifetimeIncorrect + :incorrect,
            updatedAt = :timestamp
        WHERE id = 'user_singleton'
    """)
    suspend fun incrementCounters(
        correct: Int,
        incorrect: Int,
        timestamp: Long = System.currentTimeMillis()
    )
}
```

---

### 2. QuestionStatsEntity

**Propósito**: Almacenar estadísticas agregadas por pregunta para alimentar el algoritmo dinámico.

```kotlin
package com.uquiz.android.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "question_stats",
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
data class QuestionStatsEntity(
    @PrimaryKey
    val questionId: String,

    // Contadores de intentos
    val totalAttempts: Int = 0,
    val correctAttempts: Int = 0,
    val incorrectAttempts: Int = 0,
    val timeoutAttempts: Int = 0,

    // Contadores por modo
    val studyAttempts: Int = 0,
    val gameAttempts: Int = 0,

    // Tiempos de respuesta (solo en modo GAME)
    val avgResponseTimeMs: Long? = null,
    val fastestResponseTimeMs: Long? = null,
    val slowestResponseTimeMs: Long? = null,
    val totalResponseTimeMs: Long = 0, // Para calcular promedio

    // Rachas
    val currentStreak: Int = 0,        // Respuestas correctas consecutivas actuales
    val longestStreak: Int = 0,        // Mejor racha histórica
    val currentFailStreak: Int = 0,    // Fallos consecutivos actuales

    // Nivel de dominio (calculado)
    val masteryLevel: Float = 0f,      // 0.0 - 1.0 (basado en accuracy + recencia)

    // Metadatos
    val lastAttemptedAt: Long? = null,
    val firstAttemptedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
```

### DAO de QuestionStats

```kotlin
package com.uquiz.android.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.local.entity.QuestionStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionStatsDao {

    @Query("SELECT * FROM question_stats WHERE questionId = :questionId LIMIT 1")
    suspend fun getById(questionId: String): QuestionStatsEntity?

    @Query("SELECT * FROM question_stats WHERE questionId = :questionId LIMIT 1")
    fun observeById(questionId: String): Flow<QuestionStatsEntity?>

    @Upsert
    suspend fun upsert(stats: QuestionStatsEntity)

    /**
     * Obtener preguntas que necesitan más práctica
     * (baja accuracy o no vistas recientemente)
     */
    @Query("""
        SELECT qs.* FROM question_stats qs
        WHERE qs.masteryLevel < 0.7
           OR qs.lastAttemptedAt < :thresholdTimestamp
        ORDER BY qs.masteryLevel ASC, qs.lastAttemptedAt ASC
        LIMIT :limit
    """)
    suspend fun getQuestionsNeedingPractice(
        thresholdTimestamp: Long,
        limit: Int = 10
    ): List<QuestionStatsEntity>

    /**
     * Obtener preguntas dominadas (alta accuracy + recientes)
     */
    @Query("""
        SELECT qs.* FROM question_stats qs
        WHERE qs.masteryLevel >= 0.8
        ORDER BY qs.masteryLevel DESC
        LIMIT :limit
    """)
    suspend fun getMasteredQuestions(limit: Int = 10): List<QuestionStatsEntity>

    /**
     * Estadísticas globales de todas las preguntas
     */
    @Query("""
        SELECT
            COUNT(DISTINCT questionId) as totalQuestionsAttempted,
            SUM(totalAttempts) as totalAttempts,
            SUM(correctAttempts) as totalCorrect,
            SUM(incorrectAttempts) as totalIncorrect,
            AVG(CAST(correctAttempts AS FLOAT) / NULLIF(totalAttempts, 0)) as avgAccuracy,
            AVG(avgResponseTimeMs) as avgResponseTime
        FROM question_stats
    """)
    suspend fun getGlobalQuestionStats(): GlobalQuestionStats?
}

data class GlobalQuestionStats(
    val totalQuestionsAttempted: Int,
    val totalAttempts: Int,
    val totalCorrect: Int,
    val totalIncorrect: Int,
    val avgAccuracy: Float,
    val avgResponseTime: Long?
)
```

---

### 3. PackStatsEntity

**Propósito**: Almacenar estadísticas agregadas por pack.

```kotlin
package com.uquiz.android.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pack_stats",
    foreignKeys = [
        ForeignKey(
            entity = PackEntity::class,
            parentColumns = ["id"],
            childColumns = ["packId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("packId")]
)
data class PackStatsEntity(
    @PrimaryKey
    val packId: String,

    // Contador de sesiones
    val totalStudySessions: Int = 0,
    val totalGameSessions: Int = 0,
    val completedSessions: Int = 0,
    val abandonedSessions: Int = 0,

    // Rendimiento
    val totalQuestionsAnswered: Int = 0,
    val totalCorrectAnswers: Int = 0,
    val totalIncorrectAnswers: Int = 0,
    val avgAccuracy: Float = 0f,           // 0.0 - 1.0

    // Tiempos (solo modo GAME)
    val avgSessionTimeMs: Long? = null,
    val fastestSessionTimeMs: Long? = null,
    val slowestSessionTimeMs: Long? = null,

    // Progreso y dominio
    val masteryLevel: Float = 0f,          // 0.0 - 1.0 (% preguntas dominadas)
    val totalQuestionsInPack: Int = 0,     // Cache del conteo
    val masteredQuestions: Int = 0,        // Preguntas con alta accuracy

    // Rachas
    val currentStudyStreak: Int = 0,       // Días consecutivos estudiando este pack
    val longestStudyStreak: Int = 0,

    // Metadatos
    val lastStudiedAt: Long? = null,
    val firstStudiedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
```

### DAO de PackStats

```kotlin
package com.uquiz.android.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.uquiz.android.data.local.entity.PackStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PackStatsDao {

    @Query("SELECT * FROM pack_stats WHERE packId = :packId LIMIT 1")
    suspend fun getById(packId: String): PackStatsEntity?

    @Query("SELECT * FROM pack_stats WHERE packId = :packId LIMIT 1")
    fun observeById(packId: String): Flow<PackStatsEntity?>

    @Upsert
    suspend fun upsert(stats: PackStatsEntity)

    /**
     * Obtener packs más estudiados
     */
    @Query("""
        SELECT ps.* FROM pack_stats ps
        ORDER BY (ps.totalStudySessions + ps.totalGameSessions) DESC
        LIMIT :limit
    """)
    suspend fun getMostStudiedPacks(limit: Int = 5): List<PackStatsEntity>

    /**
     * Obtener packs que necesitan más práctica
     */
    @Query("""
        SELECT ps.* FROM pack_stats ps
        WHERE ps.masteryLevel < 0.7
           OR ps.lastStudiedAt < :thresholdTimestamp
        ORDER BY ps.masteryLevel ASC, ps.lastStudiedAt ASC NULLS LAST
        LIMIT :limit
    """)
    suspend fun getPacksNeedingPractice(
        thresholdTimestamp: Long,
        limit: Int = 5
    ): List<PackStatsEntity>

    /**
     * Obtener todos los packs con sus estadísticas
     */
    @Query("""
        SELECT
            p.id,
            p.title,
            COALESCE(ps.masteryLevel, 0.0) as masteryLevel,
            COALESCE(ps.totalStudySessions, 0) as totalSessions,
            COALESCE(ps.avgAccuracy, 0.0) as accuracy
        FROM packs p
        LEFT JOIN pack_stats ps ON p.id = ps.packId
        WHERE p.folderId = :folderId
        ORDER BY p.title ASC
    """)
    fun observePacksInFolderWithStats(folderId: String): Flow<List<PackWithStats>>
}

data class PackWithStats(
    val id: String,
    val title: String,
    val masteryLevel: Float,
    val totalSessions: Int,
    val accuracy: Float
)
```

---

## Algoritmos Propuestos

### 1. Algoritmo de Tiempo Dinámico (Versión 2)

Mejorado con respecto a la versión básica del documento ARCHITECTURE_ANALYSIS.md.

```kotlin
package com.uquiz.android.domain.usecase

class CalculateDynamicTimeLimitUseCase(
    private val questionStatsRepository: QuestionStatsRepository
) {

    suspend operator fun invoke(
        question: Question,
        userRank: UserRank
    ): Long {
        // 1. Tiempo base según dificultad
        val baseTime = when (question.difficulty) {
            DifficultyLevel.EASY -> 15_000L      // 15 segundos
            DifficultyLevel.MEDIUM -> 20_000L    // 20 segundos
            DifficultyLevel.HARD -> 25_000L      // 25 segundos
            DifficultyLevel.EXPERT -> 30_000L    // 30 segundos
        }

        // 2. Ajuste por longitud de contenido
        val textLength = question.text.length +
            question.options.sumOf { it.text.length }
        val lengthBonus = (textLength / 100) * 1000L  // +1s por cada 100 caracteres

        // 3. Ajuste según historial del usuario con esta pregunta
        val stats = questionStatsRepository.getById(question.id)
        val personalAdjustment = calculatePersonalAdjustment(stats, baseTime)

        // 4. Ajuste según el rango del usuario (usuarios avanzados = menos tiempo)
        val rankMultiplier = when (userRank) {
            UserRank.U_INITIATE, UserRank.U_NEOPHYTE -> 1.2f  // +20% tiempo
            UserRank.U_ACOLYTE, UserRank.U_DISCIPLE -> 1.0f   // Tiempo normal
            UserRank.U_ADEPT, UserRank.U_VIRTUOSO -> 0.9f     // -10% tiempo
            UserRank.U_ARCHON, UserRank.U_PARAGON -> 0.8f     // -20% tiempo
        }

        val finalTime = ((baseTime + lengthBonus + personalAdjustment) * rankMultiplier).toLong()

        // 5. Límites mínimo y máximo
        return finalTime.coerceIn(5_000L, 60_000L)  // Entre 5s y 60s
    }

    private fun calculatePersonalAdjustment(
        stats: QuestionStats?,
        baseTime: Long
    ): Long {
        if (stats == null || stats.gameAttempts == 0) {
            return 0L // Primera vez viendo la pregunta en modo GAME
        }

        return when {
            // Si el usuario domina la pregunta (alta accuracy, rápido)
            stats.avgAccuracy > 0.8f &&
            stats.avgResponseTimeMs != null &&
            stats.avgResponseTimeMs < baseTime * 0.7 -> {
                -3000L // Reducir 3 segundos
            }

            // Si el usuario tiene dificultades (baja accuracy)
            stats.avgAccuracy < 0.5f -> {
                +5000L // Dar 5 segundos extra
            }

            // Si es lento pero acertado
            stats.avgAccuracy > 0.7f &&
            stats.avgResponseTimeMs != null &&
            stats.avgResponseTimeMs > baseTime * 1.2 -> {
                +2000L // Dar 2 segundos extra
            }

            else -> 0L
        }
    }
}
```

---

### 2. Algoritmo de Cálculo de Puntos

```kotlin
package com.uquiz.android.domain.usecase

class CalculatePointsUseCase {

    operator fun invoke(
        isCorrect: Boolean,
        timeMs: Long,
        timeLimitMs: Long,
        difficulty: DifficultyLevel,
        currentStreak: Int
    ): Int {
        if (!isCorrect) {
            // Penalización por respuesta incorrecta
            return -5
        }

        // 1. Puntos base según dificultad
        val basePoints = when (difficulty) {
            DifficultyLevel.EASY -> 10
            DifficultyLevel.MEDIUM -> 20
            DifficultyLevel.HARD -> 35
            DifficultyLevel.EXPERT -> 50
        }

        // 2. Bonus por velocidad (responder en menos del 50% del tiempo = x2)
        val speedRatio = timeMs.toFloat() / timeLimitMs
        val speedMultiplier = when {
            speedRatio < 0.3f -> 2.0f   // Ultra rápido: x2
            speedRatio < 0.5f -> 1.5f   // Muy rápido: x1.5
            speedRatio < 0.7f -> 1.2f   // Rápido: x1.2
            speedRatio < 0.9f -> 1.0f   // Normal: x1
            else -> 0.8f                 // Lento: x0.8
        }

        // 3. Bonus por racha
        val streakBonus = when {
            currentStreak >= 10 -> 20
            currentStreak >= 5 -> 10
            currentStreak >= 3 -> 5
            else -> 0
        }

        val totalPoints = (basePoints * speedMultiplier).toInt() + streakBonus

        return totalPoints.coerceAtLeast(1) // Mínimo 1 punto
    }
}
```

---

### 3. Algoritmo de Nivel de Dominio (Mastery Level)

```kotlin
package com.uquiz.android.domain.usecase

import kotlin.math.exp

class CalculateMasteryLevelUseCase {

    /**
     * Calcula el nivel de dominio de una pregunta (0.0 - 1.0)
     *
     * Factores:
     * - Accuracy reciente (70% del peso)
     * - Recencia (20% del peso)
     * - Consistencia (10% del peso)
     */
    operator fun invoke(stats: QuestionStats): Float {
        if (stats.totalAttempts == 0) return 0f

        // 1. Accuracy (peso: 0.7)
        val accuracy = stats.correctAttempts.toFloat() / stats.totalAttempts
        val accuracyScore = accuracy * 0.7f

        // 2. Recencia (peso: 0.2) - Curva de olvido exponencial
        val recencyScore = if (stats.lastAttemptedAt != null) {
            val daysSinceLastAttempt =
                (System.currentTimeMillis() - stats.lastAttemptedAt) / (1000 * 60 * 60 * 24)

            // Curva de olvido: e^(-t/7) donde t = días
            // Después de 7 días, el score de recencia cae a ~37%
            val recencyFactor = exp(-daysSinceLastAttempt.toFloat() / 7f)
            recencyFactor * 0.2f
        } else {
            0f
        }

        // 3. Consistencia (peso: 0.1) - Basada en la racha más larga
        val consistencyScore = if (stats.totalAttempts >= 5) {
            (stats.longestStreak.toFloat() / stats.totalAttempts).coerceAtMost(1f) * 0.1f
        } else {
            0f // Muy pocos intentos para medir consistencia
        }

        val masteryLevel = accuracyScore + recencyScore + consistencyScore

        return masteryLevel.coerceIn(0f, 1f)
    }
}
```

---

### 4. Actualización de Estadísticas después de una Respuesta

```kotlin
package com.uquiz.android.domain.usecase

class UpdateQuestionStatsUseCase(
    private val questionStatsRepository: QuestionStatsRepository,
    private val calculateMasteryLevelUseCase: CalculateMasteryLevelUseCase
) {

    suspend operator fun invoke(
        questionId: String,
        isCorrect: Boolean,
        timeMs: Long,
        mode: AttemptMode
    ) {
        // Obtener estadísticas actuales (o crear nuevas)
        val currentStats = questionStatsRepository.getById(questionId)
            ?: QuestionStats(questionId = questionId)

        // Actualizar contadores
        val updatedStats = currentStats.copy(
            totalAttempts = currentStats.totalAttempts + 1,
            correctAttempts = if (isCorrect) {
                currentStats.correctAttempts + 1
            } else {
                currentStats.correctAttempts
            },
            incorrectAttempts = if (!isCorrect) {
                currentStats.incorrectAttempts + 1
            } else {
                currentStats.incorrectAttempts
            },

            // Actualizar contadores por modo
            studyAttempts = if (mode == AttemptMode.STUDY) {
                currentStats.studyAttempts + 1
            } else {
                currentStats.studyAttempts
            },
            gameAttempts = if (mode == AttemptMode.GAME) {
                currentStats.gameAttempts + 1
            } else {
                currentStats.gameAttempts
            },

            // Actualizar tiempos (solo en modo GAME)
            avgResponseTimeMs = if (mode == AttemptMode.GAME) {
                val totalTime = currentStats.totalResponseTimeMs + timeMs
                totalTime / (currentStats.gameAttempts + 1)
            } else {
                currentStats.avgResponseTimeMs
            },
            totalResponseTimeMs = if (mode == AttemptMode.GAME) {
                currentStats.totalResponseTimeMs + timeMs
            } else {
                currentStats.totalResponseTimeMs
            },
            fastestResponseTimeMs = if (mode == AttemptMode.GAME) {
                minOf(
                    currentStats.fastestResponseTimeMs ?: Long.MAX_VALUE,
                    timeMs
                )
            } else {
                currentStats.fastestResponseTimeMs
            },
            slowestResponseTimeMs = if (mode == AttemptMode.GAME) {
                maxOf(
                    currentStats.slowestResponseTimeMs ?: 0L,
                    timeMs
                )
            } else {
                currentStats.slowestResponseTimeMs
            },

            // Actualizar rachas
            currentStreak = if (isCorrect) {
                currentStats.currentStreak + 1
            } else {
                0 // Romper racha
            },
            longestStreak = if (isCorrect) {
                maxOf(
                    currentStats.longestStreak,
                    currentStats.currentStreak + 1
                )
            } else {
                currentStats.longestStreak
            },
            currentFailStreak = if (!isCorrect) {
                currentStats.currentFailStreak + 1
            } else {
                0
            },

            // Metadatos
            lastAttemptedAt = System.currentTimeMillis(),
            firstAttemptedAt = currentStats.firstAttemptedAt
                ?: System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        // Recalcular nivel de dominio
        val finalStats = updatedStats.copy(
            masteryLevel = calculateMasteryLevelUseCase(updatedStats)
        )

        // Guardar
        questionStatsRepository.save(finalStats)
    }
}
```

---

### 5. Actualización de Rango del Usuario

```kotlin
package com.uquiz.android.domain.usecase

class UpdateUserRankUseCase(
    private val userRankRepository: UserRankRepository
) {

    suspend operator fun invoke(earnedPoints: Int): RankChangeResult {
        val current = userRankRepository.get()
            ?: UserRank.createNew() // Primera vez

        val newTotalPoints = (current.totalPoints + earnedPoints).coerceAtLeast(0)
        val newRank = UserRank.fromPoints(newTotalPoints)

        val rankChanged = newRank != current.currentRank
        val rankUp = rankChanged && newRank.ordinal > current.currentRank.ordinal
        val rankDown = rankChanged && newRank.ordinal < current.currentRank.ordinal

        val updated = current.copy(
            currentRank = newRank,
            totalPoints = newTotalPoints,
            rankPoints = newTotalPoints - newRank.minPoints,
            updatedAt = System.currentTimeMillis()
        )

        userRankRepository.save(updated)

        return RankChangeResult(
            previousRank = current.currentRank,
            newRank = newRank,
            rankChanged = rankChanged,
            rankUp = rankUp,
            rankDown = rankDown,
            pointsEarned = earnedPoints,
            totalPoints = newTotalPoints
        )
    }
}

data class RankChangeResult(
    val previousRank: UserRank,
    val newRank: UserRank,
    val rankChanged: Boolean,
    val rankUp: Boolean,
    val rankDown: Boolean,
    val pointsEarned: Int,
    val totalPoints: Int
)
```

---

## Ejemplos de Implementación

### Ejemplo 1: Flujo Completo de Respuesta en Modo GAME

```kotlin
// GameViewModel.kt

suspend fun submitAnswer(selectedOptionId: String?) {
    val currentQuestion = _uiState.value.currentQuestion ?: return
    val timeElapsed = stopTimer()
    val timeLimit = _uiState.value.timeLimit

    // 1. Determinar si es correcta
    val isCorrect = selectedOptionId?.let { optionId ->
        currentQuestion.options.find { it.id == optionId }?.isCorrect
    } ?: false

    // 2. Guardar la respuesta en AttemptAnswer
    val answer = AttemptAnswer(
        id = UUID.randomUUID().toString(),
        attemptId = currentAttemptId,
        questionId = currentQuestion.id,
        pickedOptionId = selectedOptionId,
        isCorrect = isCorrect,
        timeMs = timeElapsed,
        timeLimitMs = timeLimit
    )
    attemptRepository.saveAnswer(answer)

    // 3. Actualizar estadísticas de la pregunta
    updateQuestionStatsUseCase(
        questionId = currentQuestion.id,
        isCorrect = isCorrect,
        timeMs = timeElapsed,
        mode = AttemptMode.GAME
    )

    // 4. Calcular puntos ganados
    val questionStats = questionStatsRepository.getById(currentQuestion.id)
    val points = calculatePointsUseCase(
        isCorrect = isCorrect,
        timeMs = timeElapsed,
        timeLimitMs = timeLimit,
        difficulty = currentQuestion.difficulty,
        currentStreak = questionStats?.currentStreak ?: 0
    )

    // 5. Actualizar rango del usuario
    val rankResult = updateUserRankUseCase(points)

    // 6. Actualizar UI
    _uiState.update { current ->
        current.copy(
            score = current.score + points.coerceAtLeast(0),
            answers = current.answers + answer,
            showFeedback = true,
            feedbackIsCorrect = isCorrect,
            pointsEarned = points
        )
    }

    // 7. Mostrar animación de subida de rango si ocurrió
    if (rankResult.rankUp) {
        _events.emit(GameEvent.RankUp(rankResult.newRank))
    }

    // 8. Avanzar a la siguiente pregunta después de un delay
    delay(2000)
    loadNextQuestion()
}
```

---

### Ejemplo 2: Vista de Estadísticas de un Pack

```kotlin
// PackStatsViewModel.kt

@HiltViewModel
class PackStatsViewModel @Inject constructor(
    private val packStatsRepository: PackStatsRepository,
    private val questionStatsRepository: QuestionStatsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val packId: String = checkNotNull(savedStateHandle["packId"])

    val packStats: StateFlow<PackStatsUiState> =
        packStatsRepository.observeById(packId)
            .combine(
                questionStatsRepository.observeByPackId(packId)
            ) { packStats, questionStats ->
                PackStatsUiState.Success(
                    packStats = packStats,
                    questionStats = questionStats,
                    weakestQuestions = questionStats
                        .filter { it.masteryLevel < 0.5f }
                        .sortedBy { it.masteryLevel }
                        .take(5),
                    masteredQuestions = questionStats
                        .filter { it.masteryLevel >= 0.8f }
                        .sortedByDescending { it.masteryLevel }
                        .take(5)
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = PackStatsUiState.Loading
            )
}

sealed interface PackStatsUiState {
    object Loading : PackStatsUiState

    data class Success(
        val packStats: PackStats,
        val questionStats: List<QuestionStats>,
        val weakestQuestions: List<QuestionStats>,
        val masteredQuestions: List<QuestionStats>
    ) : PackStatsUiState
}
```

---

### Ejemplo 3: Recomendación de Packs para Practicar

```kotlin
// HomeViewModel.kt

suspend fun loadRecommendations() {
    val thresholdDate = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000) // 7 días

    val recommendations = packStatsRepository.getPacksNeedingPractice(
        thresholdTimestamp = thresholdDate,
        limit = 3
    )

    _uiState.update { current ->
        current.copy(
            recommendedPacks = recommendations.map { stats ->
                RecommendedPack(
                    packId = stats.packId,
                    reason = when {
                        stats.masteryLevel < 0.3f ->
                            "Necesitas practicar más este pack"
                        stats.lastStudiedAt == null ->
                            "Aún no has estudiado este pack"
                        stats.lastStudiedAt < thresholdDate ->
                            "No has revisado este pack en ${getDaysSince(stats.lastStudiedAt)} días"
                        else ->
                            "Repasa para mantener tu dominio"
                    },
                    masteryLevel = stats.masteryLevel
                )
            }
        )
    }
}
```

---

## Próximos Pasos

### Implementación Sugerida (Orden de Prioridad)

#### Fase 1: Entidades y DAOs (Sprint actual)
1. ✅ Crear `UserRankEntity` y `UserRank` enum
2. ✅ Crear `QuestionStatsEntity`
3. ✅ Crear `PackStatsEntity`
4. ✅ Crear los 3 DAOs correspondientes
5. ✅ Actualizar `Converters.kt` para soportar `UserRank`
6. ✅ Actualizar `UQuizDatabase` (versión 3, agregar 3 entidades)
7. ✅ Crear migración `MIGRATION_2_3`

#### Fase 2: Domain Layer
1. ✅ Crear modelos de dominio para las 3 entidades
2. ✅ Crear interfaces de repositorios
3. ✅ Crear mappers (Entity ↔ Domain)

#### Fase 3: Repositorios y Use Cases
1. ✅ Implementar `UserRankRepositoryImpl`
2. ✅ Implementar `QuestionStatsRepositoryImpl`
3. ✅ Implementar `PackStatsRepositoryImpl`
4. ✅ Crear los 5 use cases propuestos

#### Fase 4: Integración con Attempts
1. ✅ Modificar `GameViewModel` para actualizar stats
2. ✅ Modificar `StudyViewModel` para actualizar stats
3. ✅ Crear listeners de eventos para actualización automática

#### Fase 5: UI de Estadísticas
1. ✅ Crear `UserRankScreen` (perfil con rango actual)
2. ✅ Crear `PackStatsScreen` (estadísticas detalladas por pack)
3. ✅ Agregar badges/indicadores de rango en UI
4. ✅ Agregar animaciones de subida de rango

#### Fase 6: Optimizaciones
1. ✅ Agregar índices adicionales si es necesario
2. ✅ Implementar batch updates para mejor rendimiento
3. ✅ Agregar caché en memoria para datos frecuentes

---

### Dependencias Técnicas

#### Actualización de `build.gradle.kts`

No se requieren nuevas dependencias, todo se puede hacer con:
- Room (ya incluido)
- Hilt (ya incluido)
- Kotlin Coroutines (ya incluido)

---

### Testing

#### Tests Requeridos

1. **DAOs**:
   - CRUD básico de cada entidad
   - Queries complejas
   - Cascadas de eliminación

2. **Use Cases**:
   - `CalculateDynamicTimeLimitUseCase` con diferentes escenarios
   - `CalculatePointsUseCase` con todas las combinaciones
   - `CalculateMasteryLevelUseCase` con casos edge
   - `UpdateQuestionStatsUseCase` con múltiples llamadas
   - `UpdateUserRankUseCase` con subidas y bajadas de rango

3. **Repositorios**:
   - Integración con DAOs
   - Manejo de errores
   - Flows reactivos

4. **ViewModels**:
   - Estados correctos según datos
   - Eventos de UI
   - Manejo de ciclo de vida

---

## Consideraciones Adicionales

### Performance

- Las actualizaciones de stats deben ser **asíncronas** para no bloquear la UI
- Considerar usar `WorkManager` para cálculos pesados en background
- Implementar índices en todas las foreign keys

### Privacidad

- Todos los datos son locales (offline-first)
- No hay tracking externo
- El usuario es dueño de sus datos

### Extensibilidad Futura

Esta estructura permite agregar fácilmente:
- **Achievements/Badges**: Basados en stats actuales
- **Leaderboards locales**: Comparar con amigos
- **Modo Arcade**: Desafíos con límite de tiempo
- **Spaced Repetition**: Usar `lastAttemptedAt` y `masteryLevel`
- **Analytics avanzados**: Machine learning sobre los datos

---

## Referencias

- [Room Database - Foreign Keys](https://developer.android.com/training/data-storage/room/referencing-data)
- [MVVM Architecture Guide](https://developer.android.com/topic/architecture)
- [Kotlin Flows](https://kotlinlang.org/docs/flow.html)
- [Spaced Repetition Algorithms](https://en.wikipedia.org/wiki/Spaced_repetition)

---

**Documento Creado**: 2026-01-28
**Autor**: Análisis técnico para UQuiz MVP
**Estado**: Propuesta lista para revisión e implementación
