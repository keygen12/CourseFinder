package com.example.coursefinder.data

/**
 * Заранее подготовленный каталог курсов для учебной версии проекта
 * (см. раздел 1.3 курсовой: "допускается использование заранее
 * подготовленного каталога курсов... без обязательного подключения
 * к внешним API"). Загружается один раз при первом создании базы данных.
 */
object SeedData {
    val courses = listOf(
        Course(
            title = "Python для начинающих",
            platform = "Stepik",
            category = "Программирование",
            level = "Начальный",
            language = "Русский",
            format = "Видео",
            price = 0,
            durationHours = 20,
            description = "Базовый синтаксис Python, типы данных, циклы, функции. Подходит для старта без опыта программирования."
        ),
        Course(
            title = "Алгоритмы и структуры данных",
            platform = "Coursera",
            category = "Программирование",
            level = "Средний",
            language = "Английский",
            format = "Видео",
            price = 4500,
            durationHours = 40,
            description = "Классический курс по алгоритмам: сортировки, деревья, графы, жадные алгоритмы и динамическое программирование."
        ),
        Course(
            title = "Kotlin для Android-разработки",
            platform = "Udemy",
            category = "Мобильная разработка",
            level = "Начальный",
            language = "Русский",
            format = "Видео",
            price = 3200,
            durationHours = 30,
            description = "Основы Kotlin и разработка первых Android-приложений с использованием Jetpack Compose."
        ),
        Course(
            title = "Android Jetpack Compose на практике",
            platform = "Udemy",
            category = "Мобильная разработка",
            level = "Средний",
            language = "Русский",
            format = "Видео",
            price = 5000,
            durationHours = 35,
            description = "Построение современных Android-интерфейсов на Jetpack Compose, навигация, работа с состоянием."
        ),
        Course(
            title = "SQL с нуля",
            platform = "Stepik",
            category = "Базы данных",
            level = "Начальный",
            language = "Русский",
            format = "Текст",
            price = 0,
            durationHours = 15,
            description = "Основы реляционных баз данных и языка SQL: выборки, соединения таблиц, агрегатные функции."
        ),
        Course(
            title = "Проектирование баз данных",
            platform = "Coursera",
            category = "Базы данных",
            level = "Средний",
            language = "Английский",
            format = "Видео",
            price = 3800,
            durationHours = 25,
            description = "Нормализация, ER-модели, индексы и проектирование схем для реальных приложений."
        ),
        Course(
            title = "Английский для IT-специалистов",
            platform = "Skyeng",
            category = "Иностранные языки",
            level = "Средний",
            language = "Русский",
            format = "Интерактив",
            price = 6000,
            durationHours = 50,
            description = "Технический английский: чтение документации, собеседования, общение в международной команде."
        ),
        Course(
            title = "Английский язык: разговорный уровень A2",
            platform = "Duolingo",
            category = "Иностранные языки",
            level = "Начальный",
            language = "Русский",
            format = "Интерактив",
            price = 0,
            durationHours = 40,
            description = "Базовая разговорная практика и грамматика английского языка для начинающих."
        ),
        Course(
            title = "UI/UX-дизайн: с чего начать",
            platform = "Udemy",
            category = "Дизайн",
            level = "Начальный",
            language = "Русский",
            format = "Видео",
            price = 2500,
            durationHours = 18,
            description = "Принципы пользовательского интерфейса, основы Figma, прототипирование экранов."
        ),
        Course(
            title = "Figma для продуктового дизайнера",
            platform = "Udemy",
            category = "Дизайн",
            level = "Средний",
            language = "Русский",
            format = "Видео",
            price = 4000,
            durationHours = 22,
            description = "Компоненты, автолейауты, дизайн-системы и совместная работа в Figma."
        ),
        Course(
            title = "Введение в DevOps",
            platform = "Stepik",
            category = "DevOps",
            level = "Начальный",
            language = "Русский",
            format = "Видео",
            price = 0,
            durationHours = 20,
            description = "CI/CD, контейнеризация, базовые принципы автоматизации развёртывания приложений."
        ),
        Course(
            title = "Docker и Kubernetes на практике",
            platform = "Coursera",
            category = "DevOps",
            level = "Продвинутый",
            language = "Английский",
            format = "Видео",
            price = 5500,
            durationHours = 45,
            description = "Контейнеризация приложений, оркестрация и развёртывание в Kubernetes-кластере."
        ),
        Course(
            title = "Машинное обучение: основы",
            platform = "Coursera",
            category = "Data Science",
            level = "Средний",
            language = "Английский",
            format = "Видео",
            price = 4800,
            durationHours = 55,
            description = "Линейная регрессия, классификация, основы нейронных сетей на практических примерах."
        ),
        Course(
            title = "Анализ данных на Python",
            platform = "Stepik",
            category = "Data Science",
            level = "Средний",
            language = "Русский",
            format = "Видео",
            price = 2900,
            durationHours = 30,
            description = "Pandas, NumPy, визуализация данных и основы статистического анализа."
        ),
        Course(
            title = "Основы кибербезопасности",
            platform = "Udemy",
            category = "Безопасность",
            level = "Начальный",
            language = "Русский",
            format = "Видео",
            price = 3000,
            durationHours = 20,
            description = "Базовые принципы защиты информации, типы атак и способы противодействия."
        ),
        Course(
            title = "Git и GitHub для командной разработки",
            platform = "Stepik",
            category = "Программирование",
            level = "Начальный",
            language = "Русский",
            format = "Текст",
            price = 0,
            durationHours = 10,
            description = "Система контроля версий Git, работа с ветками, pull request и совместная разработка."
        )
    )
}
