Features

User Authentication & Security:

Secure user registration and login system using BCrypt for password hashing.

Email Verification: New user registrations require a 6-digit verification code sent via email (JavaMail API).

AI & Document Processing:

Text Extraction: Uses Apache Tika to extract raw text from various formats like PDF and PowerPoint (PPT/PPTX).

Smart Summarization: Integrated with Google Gemini 1.5 Flash Model to analyze text and generate concise summaries instantly.

Multi-Language Support:

Full support for Turkish (TR) and English (EN) with instant switching capabilities.

All UI text is managed dynamically via ThemeManager.java.

Modern UI & Dynamic Theming:

Toggle between a Dark Mode and a Light Mode.

Features a custom Swing design with flattened buttons, modern borders, and gradient backgrounds.

Database & Logging:

Activity Logging: Every document process is logged into the database with timestamps for tracking history.

DatabaseManager.java automatically initializes the database and creates required tables (users, processed_documents) on first launch.

🛠️ Tech Stack

Frontend: Java Swing (Customized UI)

Backend: Java 17+

Database: Microsoft SQL Server

AI Integration: Google Gemini API (via OkHttp & Gson)

Libraries: Apache Tika, jBCrypt, JavaMail API
