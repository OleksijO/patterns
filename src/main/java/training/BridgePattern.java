package training;

/**
 * Created by oleksij.onysymchuk@gmail on 26.11.2016.
 */
public class BridgePattern {

    public static void main(String[] args) {
        ReportBuilder reportBuilder = new WeekReportBuilder(new WordDocumentSaver());
        reportBuilder.placeDataToReport(new Data());
        reportBuilder.save();
        reportBuilder.setDocumentSaver(new PdfDocumentSaver());
        reportBuilder.save();

        BaseReportBuilder baseReportBuilder = new BaseReportBuilder(new WordDocumentSaver());
        baseReportBuilder.placeDataToReport(new Data());
        baseReportBuilder.save();
        baseReportBuilder.setDocumentSaver(new PdfDocumentSaver());
        baseReportBuilder.save();
    }

    interface Document {
    }

    interface DocumentSaver {
        void save(Document document);
    }

    interface ReportBuilder {
        void placeDataToReport(Data data);

        void save();

        void setDocumentSaver(DocumentSaver saver);
    }

    static class BaseReport implements Document {
    }

    static class WeekReport extends BaseReport {
    }

    static class WeekReportBuilder implements ReportBuilder {
        private DocumentSaver saver;
        private Document weekReport = new WeekReport();

        public WeekReportBuilder(DocumentSaver saver) {
            this.saver = saver;
        }

        @Override
        public void placeDataToReport(Data data) {
            // placing data to fields of document
        }

        @Override
        public void save() {
            saver.save(weekReport);
        }

        @Override
        public void setDocumentSaver(DocumentSaver saver) {
            this.saver = saver;
        }
    }

    static class BaseReportBuilder implements ReportBuilder {
        private DocumentSaver saver;
        private Document baseReport = new BaseReport();

        public BaseReportBuilder(DocumentSaver saver) {
            this.saver = saver;
        }

        @Override
        public void placeDataToReport(Data data) {
            // placing data to fields of document
        }

        @Override
        public void save() {
            saver.save(baseReport);
        }

        @Override
        public void setDocumentSaver(DocumentSaver saver) {
            this.saver = saver;
        }
    }

    static class WordDocumentSaver implements DocumentSaver {
        @Override
        public void save(Document document) {
            System.out.println("Saving " + document.getClass().getSimpleName() + " in MS WORD FORMAT");
        }
    }

    static class PdfDocumentSaver implements DocumentSaver {
        @Override
        public void save(Document document) {
            System.out.println("Saving " + document.getClass().getSimpleName() + " in PDF FORMAT");
        }
    }

    static class Data {
    }
}




