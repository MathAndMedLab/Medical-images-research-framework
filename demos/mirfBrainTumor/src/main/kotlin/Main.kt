import javax.swing.SwingUtilities
import javax.swing.UIManager

object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        SwingUtilities.invokeLater {
            val form = MainForm()
            form.isVisible = true
        }
    }


}