package rw.jar2db;

import org.junit.Ignore;

public class Scrapbook {
    @org.junit.Test
    @Ignore
    public void test() throws Exception {
        try (ModelCreator modelCreator = new ModelCreator("target/.tmp.db")) {
            TestData.addClasses(modelCreator.classSources);
            modelCreator.export();
        }
    }
}
