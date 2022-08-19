package javafxlibrary.utils.HelperFunctionsTests;

import javafx.geometry.BoundingBox;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.testfx.service.query.BoundsQuery;

import java.util.ArrayList;
import java.util.List;

@Ignore("Fails when run with Maven")
public class CheckClickTargetTest extends TestFxAdapterTest {

    private List<Window> windows;
    private Button button;

    @Mocked
    Stage stage;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        windows = new ArrayList<>();
        windows.add(stage);
        button = new Button();
        HelperFunctions.setLibraryKeywordTimeout(0);
    }

    private void setupStageTests(int x, int y, int width, int height) {
        new Expectations() {
            {
                getRobot().listWindows();
                result = windows;
                stage.isShowing();
                result = true;
                stage.getX();
                result = 250;
                stage.getY();
                result = 250;
                stage.getWidth();
                result = 250;
                stage.getHeight();
                result = 250;
                getRobot().bounds((Button) any);
                BoundsQuery bq = () -> new BoundingBox(x, y, width, height);
                result = bq;
            }
        };
    }

    @Test
    public void checkClickTarget_WithinVisibleWindow() {
        setupStageTests(300, 300, 50, 50);
        Button result = (Button) HelperFunctions.checkClickTarget(button);
        Assert.assertEquals(button, result);
    }

    @Test
    public void checkClickTarget_OutsideVisibleWindow() {
        setupStageTests(480, 480, 50, 50);
        String target = "object inside active window check failed: can't click Button at [505.0, 505.0]: out of window " +
            "bounds. To enable clicking outside of visible window bounds use keyword `Set Safe Clicking` with argument `off`";

        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage(target);
        HelperFunctions.checkClickTarget(button);
    }

    @Test
    public void checkClickTarget_UsingStringLocator() throws InterruptedException {
        new MockUp<HelperFunctions>() {
            @Mock
            Node objectToNode(Object target) {
                return button;
            }
        };
        HelperFunctions.setLibraryKeywordTimeout(1);
        setupStageTests(300, 300, 50, 50);
        Button result = (Button) HelperFunctions.checkClickTarget("css=.button");
        Assert.assertEquals(button, result);
    }

    @Test
    public void checkClickTarget_Disabled() {
        button.setDisable(true);
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("target \"" + button + "\" not enabled!");
        HelperFunctions.checkClickTarget(button);
    }

    @Test
    public void checkClickTarget_NotVisible() {
        button.setVisible(false);
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("target \"" + button + "\" not visible");
        HelperFunctions.checkClickTarget(button);
    }
}
