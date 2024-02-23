package com.solana.debugger.toolWindow

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader.getIcon
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import org.jetbrains.plugins.template.toolWindow.MyToolWindowFactory
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.io.BufferedReader
import java.io.File
import java.util.*
import javax.swing.*


internal class DebuggerToolWindowFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val toolWindowContent = DebuggerToolWindowContent(toolWindow)
        val content = ContentFactory.getInstance().createContent(toolWindowContent.contentPanel, "", false)
        toolWindow.contentManager.addContent(content)
    }

    private class DebuggerToolWindowContent(toolWindow: ToolWindow) {
        val contentPanel: JPanel = JPanel()
        private val currentOutput = JTextArea()

        init {
            contentPanel.layout = BorderLayout(0, 20)
            contentPanel.border = BorderFactory.createEmptyBorder(20, 20, 20, 20)
            contentPanel.add(createOutputPanel(), BorderLayout.CENTER)
            contentPanel.add(createControlsPanel(toolWindow), BorderLayout.NORTH)
        }

        private fun createOutputPanel(): JPanel {
            val outputPanel = JPanel(BorderLayout())
            currentOutput.isEditable = false
            currentOutput.text = "Click on 'Refresh' to produce output"

            //val scrollPane = JScrollPane(currentOutput)
            //outputPanel.add(scrollPane)
            val scrollPane = JBScrollPane(currentOutput)
            outputPanel.add(scrollPane, BorderLayout.CENTER)

            //outputPanel.add(currentOutput)
            return outputPanel
        }

        private fun createControlsPanel(toolWindow: ToolWindow): JPanel {
            val controlsPanel = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0))

            val refreshButton = JButton("Refresh")
            refreshButton.addActionListener { e: ActionEvent? -> updateCurrentOutput() }
            controlsPanel.add(refreshButton)

            controlsPanel.add(Box.createRigidArea(Dimension(10, 0)))

            val hideButton = JButton("Hide")
            hideButton.addActionListener { e: ActionEvent? -> toolWindow.hide(null) }
            controlsPanel.add(hideButton)

            return controlsPanel
        }

        private fun updateCurrentOutput() {

            val userHome = System.getProperty("user.home")
            println("User's home directory: $userHome")

            val lldbClientPath = "$userHome/.local/share/solana/install/active_release/bin/sdk/sbf/dependencies/platform-tools/llvm/bin/solana-lldb"

            println("File path: $lldbClientPath")
            val file = File(lldbClientPath)

            if (file.exists() && file.canExecute()) {
                println("The file exists and is executable")
            } else {
                println("The file does not exist or is not executable")

                currentOutput.text = "The file does not exist or is not executable."
                return
            }

            // Command to be executed

            val command = listOf(lldbClientPath, "--batch", "-o", "shell date +%c")

            val processBuilder = ProcessBuilder(command)

            processBuilder.redirectErrorStream(true)

            // Run process

            try {
                val process = processBuilder.start()

                // Reading the output of the command
                val reader = BufferedReader(process.inputStream.reader())
                val output = reader.readText()

                // Printing the output to the console
                println("Output from the external program: $output")

                currentOutput.text = output

                // Waiting for the external program to finish
                val exitCode = process.waitFor()
                println("Exited with code $exitCode")

                if (exitCode != 0)
                    throw Exception("Non-zero exit code")

            } catch (e: Exception) {
                println("Failed to execute solana-lldb")
                currentOutput.text = "Failed to execute solana-lldb."
            }

        }

        private fun getCurrentTime(calendar: Calendar): String {
            return (calendar[Calendar.HOUR_OF_DAY].toString() + ":"
                    + (calendar[Calendar.MINUTE].toString()) + ":"
                    + calendar[Calendar.SECOND].toString())
        }
    }
}