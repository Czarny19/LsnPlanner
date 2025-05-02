# LsnPlanner
Android application for lesson planning - class calendar, tests and homework

## Tech stack

`Kotlin` `Jetpack Compose` `Coroutines` `Flow` `Hilt` `Material3` `Datastore` `Room` `Supabase`
`JUnit` `Mockk`

## Data persistance

Offile data persistance done using a Room database and Android datastore.

Online data persistance done using Supabase Postgres

## Functionalities

- SignIn/SignUp (Online and offline, Password reset)
- Lesson plan list
- Lesson plans (Creation and editing, School plans and University plans, Multiple address classes)
- Notes (Full screen editor, Importance indicator)
- Class list for selected lesson plan
- Class info (With custom color picker)
- Class schedules (Weekly, singular and periodic schedules)