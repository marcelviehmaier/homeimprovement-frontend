
import de.hspf.homeimprovementfrontend.config.ViewContextUtil;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcel
 */
@Named(value = "routerBean")
@SessionScoped
public class Router implements Serializable {
    
    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final long serialVersionUID = -383992898674810212L;
    
    public Router() {
    }
    
    public void redirect(String targetPage) throws IOException {
        logger.log(Level.INFO, "Redirect user to {0}", targetPage);
        ViewContextUtil.getExternalContext().redirect(ViewContextUtil.getExternalContext().getRequestContextPath() + "/app/" + targetPage + ".xhtml");
    }
}
