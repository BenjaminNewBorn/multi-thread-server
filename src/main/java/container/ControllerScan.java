package container;

import java.util.logging.Logger;

public class ControllerScan {

    private static final Logger logger = Logger.getLogger(ControllerScan.class.getPackageName());
    private static final String CONTROLLER_ROOT_PATH = ControllerScan.class.getResource("/").toString().substring(5) + "controller/";

}
