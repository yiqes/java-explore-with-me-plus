# Функционал комментариев

Этот проект реализует функционал комментариев с разграничением доступа для разных ролей пользователей:  **Private**, **Admin**, и **Public**.

## API Endpoints

Проект предоставляет следующие API endpoints:

**Private:**

* **POST /comment/event/{eventId}**: Создает новый комментарий (Private).
* **GET /comment/{commentId}**: Возвращает информацию о конкретном комментарии (Private).
* **GET /comment/user/{userId}**: Возвращает список комментариев, доступных пользователю (Private).
* **PATCH /comment/{commentId}**: Изменяет существующий комментарий (Private).
* **DELETE /comment/{commentId}**: Удаляет комментарий (Private).


**Admin:**

* **GET /admin/comment/**: Поиск комментариев по тексту (Admin).
* **GET /admin/comment/{commentId}**: Возвращает информацию о конкретном комментарии (Admin).
* **GET /admin/comment/user/{userId}**: Возвращает список всех комментариев (Admin).
* **DELETE /admin/comment/{commentId}**: Удаляет комментарий (Admin).


**Public:**

* **GET /comment/event/{eventId}**: Возвращает список публично доступных комментариев (Public).

