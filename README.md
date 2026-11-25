# CourseFinder — Android-приложение (Kotlin + Jetpack Compose)

Локальное приложение-каталог учебных курсов: поиск, фильтры, избранное,
отзывы и оценки. Без регистрации/авторизации и без админ-панели — это
осознанно вынесено «за рамки первой версии» (см. главу 1.3 курсовой).

## Стек

- Kotlin, Jetpack Compose (Material 3)
- Room (SQLite) — локальная база с предзагруженным каталогом курсов
- Navigation Compose — переходы между тремя экранами
- Архитектура MVVM: `data` (Entity/DAO/DB) → `viewmodel` → `ui`

## Структура проекта

```
app/src/main/java/com/example/coursefinder/
├── MainActivity.kt              — точка входа, создаёт БД и ViewModel
├── data/
│   ├── Course.kt                — Room-сущность "курс"
│   ├── Review.kt                — Room-сущность "отзыв"
│   ├── CourseDao.kt             — запросы к базе
│   ├── AppDatabase.kt           — конфигурация БД + предзагрузка каталога
│   └── SeedData.kt              — демонстрационный каталог курсов
├── viewmodel/
│   └── CourseViewModel.kt       — поиск/фильтры/избранное/отзывы
└── ui/
    ├── Navigation.kt            — граф навигации (3 экрана)
    ├── theme/                   — цвета, типографика, тема Compose
    └── screens/
        ├── CourseListScreen.kt    — список, поиск, фильтры
        ├── CourseDetailScreen.kt  — карточка курса, отзывы, форма отзыва
        └── FavoritesScreen.kt     — список избранных курсов
```

## Как открыть и запустить

1. Установите [Android Studio](https://developer.android.com/studio) (последняя стабильная версия).
2. **File → Open** → выберите папку `CourseFinder` (эту, с файлом `settings.gradle.kts` внутри).
3. При первом открытии Android Studio предложит создать Gradle Wrapper —
   согласитесь (или укажите локально установленный Gradle 8.7+).
   Дайте Gradle синхронизироваться — он сам скачает все зависимости
   (Compose, Room, Navigation) из google()/mavenCentral().
4. Запустите на эмуляторе или устройстве: зелёная кнопка **Run ▶** или `Shift+F10`.
   Минимальная версия Android — 7.0 (API 24).

## Для главы 3 курсовой

- Скриншоты экранов делайте прямо из эмулятора/устройства (Ctrl+S в окне эмулятора
  или кнопка камеры на панели эмулятора).
- Структуру БД можно показать через **App Inspection → Database Inspector**
  в Android Studio (видно таблицы `courses` и `reviews` и их содержимое).
- Если при сборке появится ошибка Gradle — пришлите текст ошибки, разберём.
