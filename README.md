# Productivity-Suite-JAVA-
A desktop productivity application built entirely in **Java** using Swing and AWT. It opens a launcher window from which you can start five separate tools.

## Tools

1. **To-Do List:** add tasks with a priority, mark them done, filter and delete tasks.
2. **Expense Tracker:** record expenses with date, category, description and amount in a table, and view totals.
3. **Health & Fitness Tracker:** log activity, duration, calories and water intake, and calculate BMI.
4. **Study Planner & Pomodoro Timer:** plan study subjects (colour-coded) and use a Pomodoro timer with study and break sessions.
5. **Text Editor:** create, open and save text files, with Undo/Redo, Cut/Copy/Paste, Find & Replace, Duplicate Line and a dark mode.

## Tech stack
- Java (Swing and AWT only, no external libraries)

## How to run
1. Install a JDK (Java 8 or newer).
2. Compile and run from the project folder:
   ```
   javac ProductivitySuite.java
   java ProductivitySuite
   ```

## Limitations and future work
- To-Do, Expense, Health and Study data are kept in memory and are lost when the app closes. Only the Text Editor saves files.
- Planned: save data to files or a database, export reports, and add charts.
