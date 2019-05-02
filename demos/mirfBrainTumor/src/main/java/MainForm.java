import com.mirf.core.pipeline.PipelineSessionRecord;
import kotlin.Unit;
import org.jetbrains.annotations.Nullable;
import pdfLayouts.PatientInfo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.io.File;

public class MainForm extends JFrame{
    private JTextField urlField;
    private JTextField t1Field;
    private JTextField nameField;
    private JTextField AgeField;
    private JTextField flairLink;
    private JButton generateButton;
    private JButton chooseT1Button;
    private JButton chooseFlairButton;
    private JTextField workinDirField;
    private JButton chooseWorkinDirButton;
    private JPanel rootPanel;
    private JTable progress;


    public MainForm() {

        add(rootPanel);
        setTitle("Ms report generator");
        setSize(700, 800);

        createProgressTable();

        generateButton.addActionListener(e -> {
            BrainReportWorkflow workflow = new BrainReportWorkflow(
                    "/Users/sabrina/Documents/GitHub/brats17/data/brats17/Brats17_2013_3_1",
                    "/Users/sabrina/Documents/GitHub/brats17/data/brats17/Brats17_2013_3_1/Brats17_2013_3_1_t1.nii.gz",
//                   t1Field.getText(), flairLink.getText(),
                    workinDirField.getText(),
                    new PatientInfo(nameField.getText(), AgeField.getText()));

            workflow.getPipe().getSession().getNewRecord().plusAssign(((x, a )-> addRecord(a)));

            new Thread(workflow::exec).start();

        });

        chooseT1Button.addActionListener(e -> {
            String path = getNiiSeriesFromFileChooser();

            if(path != null)
                t1Field.setText(path);
        });

        chooseFlairButton.addActionListener(e -> {
            String path = getNiiSeriesFromFileChooser();

            if(path != null)
                flairLink.setText(path);
        });

        chooseWorkinDirButton.addActionListener(e -> {
            String path = getDirectoryFromFileChooser();

            if(path != null)
                workinDirField.setText(path);
        });
    }

    private void createProgressTable() {
        progress.setModel(new DefaultTableModel(new Object[][]{}, new Object[]{"progress"}));
    }

    private Unit addRecord(PipelineSessionRecord record) {
        DefaultTableModel model = (DefaultTableModel) progress.getModel();
        model.addRow(new Object[]{record.getMessage()});
        return Unit.INSTANCE;
    }



    private String getNiiSeriesFromFileChooser() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        FileNameExtensionFilter filter = new FileNameExtensionFilter("NIFTI series", "nii");
        jfc.addChoosableFileFilter(filter);

        return getStringFromFileChooser(jfc);
    }

    private String getDirectoryFromFileChooser(){
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        return getStringFromFileChooser(jfc);
    }

    @Nullable
    private String getStringFromFileChooser(JFileChooser jfc) {
        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return null;
    }

    @Nullable
    private String getDirectoryNameFromFileChooser(JFileChooser jfc) {
        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return null;
    }
}
