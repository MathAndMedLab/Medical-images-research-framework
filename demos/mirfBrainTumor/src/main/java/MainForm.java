import com.mirf.core.pipeline.PipelineSessionRecord;
import kotlin.Unit;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;
import org.jetbrains.annotations.Nullable;
import pdfLayouts.PatientInfo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class MainForm extends JFrame {
    private JTextField folderField;
    private JTextField nameField;
    private JTextField AgeField;
    private JTextField mainImageField;
    private JButton generateButton;
    private JButton chooseFolder;
    private JButton chooseMainImageButton;
    private JTextField workinDirField;
    private JButton chooseWorkinDirButton;
    private JPanel rootPanel;
    private JTable progress;
    private SwingController controller;

    public MainForm() {
        setTitle("Brain tumor report generation");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel flow = new JPanel(new BorderLayout());
        addMenu(flow);
        addPdfView(flow);
        Container container = getContentPane();
        container.add(flow, BorderLayout.PAGE_START);

        generateButton.addActionListener(e -> {
            BrainReportWorkflow workflow = new BrainReportWorkflow(
                    folderField.getText(), mainImageField.getText(),
                    workinDirField.getText(),
                    new PatientInfo(nameField.getText(), AgeField.getText()));

            workflow.getPipe().getSession().getNewRecord().plusAssign(((x, a) -> addRecord(a)));

            System.out.println(workinDirField.getText());
            new Thread(new Runnable() {
                public void run() {
                    workflow.exec();
                    updatePdf();
                }
            }).start();
        });

        chooseFolder.addActionListener(e -> {
            String path = getDirectoryFromFileChooser();

            if (path != null)
                folderField.setText(path);
        });

        chooseMainImageButton.addActionListener(e -> {
            String path = getNiiSeriesFromFileChooser();

            if (path != null)
                mainImageField.setText(path);
        });

        chooseWorkinDirButton.addActionListener(e -> {
            String path = getDirectoryFromFileChooser();

            if (path != null)
                workinDirField.setText(path);
        });

        // show the component
        pack();
        setVisible(true);
    }

    private void addMenu(JPanel flow) {
        JPanel menu = new JPanel(new FlowLayout());
        menu.add(rootPanel);
        createProgressTable();
        flow.add(menu, BorderLayout.NORTH);
    }

    private void addPdfView(JPanel flow) {
        controller = new SwingController();
        SwingViewBuilder factory = new SwingViewBuilder(controller);
        controller.setIsEmbeddedComponent(true);
        JPanel pdf = factory.buildViewerPanel();
        ComponentKeyBinding.install(controller, pdf);
        controller.getDocumentViewController().setAnnotationCallback(
                new org.icepdf.ri.common.MyAnnotationCallback(
                        controller.getDocumentViewController()));
        flow.add(pdf, BorderLayout.CENTER);

        controller.setPageViewMode(
                DocumentViewControllerImpl.ONE_PAGE_VIEW,
                false);
    }

    private void updatePdf() {
        controller.openDocument(workinDirField.getText() + "/report.pdf");
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
        JFileChooser jfc;
        if (mainImageField.getText() != null) {
            jfc = new JFileChooser(FileSystemView.getFileSystemView().createFileObject(folderField.getText()));
        } else {
            jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        }
        FileNameExtensionFilter filter = new FileNameExtensionFilter("NIFTI series", "nii");
        jfc.addChoosableFileFilter(filter);

        return getStringFromFileChooser(jfc);
    }

    private String getDirectoryFromFileChooser() {
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
