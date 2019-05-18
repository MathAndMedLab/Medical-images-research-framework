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
    private JTextField baselineT1Link;
    private JTextField nameField;
    private JTextField AgeField;
    private JTextField baselineFLAIRLink;
    private JButton generateButton;
    private JButton chooseT1Button;
    private JButton chooseFlairButton;
    private JTextField workinDirField;
    private JButton chooseWorkinDirButton;
    private JPanel rootPanel;
    private JTable progress;
    private JTextField followupT1Link;
    private JTextField followupFlairLink;
    private JButton chooseCurT1;
    private JButton chooseCurFlair;


    public MainForm() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(rootPanel);
        setTitle("Ms report generator");
        setSize(700, 800);

        urlField.setText("http://localhost:8080");
        baselineT1Link.setText("C:\\MS-DATA\\patient\\baseline\\T1.nii");
        baselineFLAIRLink.setText("C:\\MS-DATA\\patient\\baseline\\FLAIR.nii");
        followupT1Link.setText("C:\\MS-DATA\\patient\\followup\\T1.nii");
        followupFlairLink.setText("C:\\MS-DATA\\patient\\followup\\FLAIR.nii");
        workinDirField.setText("C:\\src\\mirf_path");
        createProgressTable();

        generateButton.addActionListener(e -> {
            MsReportWorkflow workflow = MsReportWorkflow.Companion.createFull(urlField.getText(), followupT1Link.getText(), followupFlairLink.getText(),
                    workinDirField.getText(), new PatientInfo(nameField.getText(), AgeField.getText()), baselineT1Link.getText(), baselineFLAIRLink.getText());

            workflow.getPipe().getSession().getNewRecord().plusAssign(((x, a )-> addRecord(a)));

            new Thread(workflow::exec).start();

        });

        chooseT1Button.addActionListener(e -> {
            String path = getNiiSeriesFromFileChooser();

            if(path != null)
                baselineT1Link.setText(path);
        });

        chooseFlairButton.addActionListener(e -> {
            String path = getNiiSeriesFromFileChooser();

            if(path != null)
                baselineFLAIRLink.setText(path);
        });

        chooseCurFlair.addActionListener(e -> {
            String path = getNiiSeriesFromFileChooser();

            if(path != null)
                followupFlairLink.setText(path);
        });

        chooseCurT1.addActionListener(e -> {
            String path = getNiiSeriesFromFileChooser();

            if(path != null)
                followupT1Link.setText(path);
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
}
