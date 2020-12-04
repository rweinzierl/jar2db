package rw.jar2db;

import java.io.File;

public class Test {
    @org.junit.Test
    public void test() throws Exception {
        File dbFile = File.createTempFile("jar", ".sqlite", new File("target"));
        dbFile.deleteOnExit();
        try (ModelCreator modelCreator = new ModelCreator(dbFile.getPath())) {
            TestData.addClasses(modelCreator.classSources);
            modelCreator.export();
        }
    }
}
