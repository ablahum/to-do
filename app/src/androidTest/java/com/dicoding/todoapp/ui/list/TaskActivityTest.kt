package com.dicoding.todoapp.ui.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.add.AddTaskActivity
import org.junit.After
import org.junit.Rule
import org.junit.Test

// TODO 16 : Write UI test to validate when user tap Add Task (+), the AddTaskActivity displayed
class TaskActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(TaskActivity::class.java)

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun clickFab_OpenAddTaskActivity() {
        // Initialize Espresso Intents
        Intents.init()

        // Click on the FAB to open AddTaskActivity
        onView(withId(R.id.fab)).perform(click())

        // Verify that the correct intent to launch AddTaskActivity is sent
        Intents.intended(hasComponent(AddTaskActivity::class.java.name))
    }
}
