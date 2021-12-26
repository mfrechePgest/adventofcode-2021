import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

public class Day13Gui {

    public static final int FOLD_DURATION = 2000;
    // The window handle
    private long window;
    private Day13 event;
    private List<CellDisplay> cells = null;
    private List<CellAnimation> animations = null;
    private float largeurCase = 0f;

    private boolean animate = false;
    private boolean resizeAnimation = false;
    private Long animationStart = null;
    Day13.Situation currentSituation = null;


    private final ImageParser resource_01 = ImageParser.load_image("img_2.png");


    public static void main(String[] args) throws IOException {
        new Day13Gui().run();
    }

    public void run() throws IOException {
        System.out.println("Submarine 13e jour !");


        event = new Day13("day13_1.txt");

        initSituation();

        init();

        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void initSituation() {
        currentSituation = event.getSituation();
        largeurCase = computeLargeurCase(currentSituation.maxX(), currentSituation.maxY());
        cells = currentSituation.dots().stream()
                .map(dot -> new CellDisplay(dot, largeurCase))
                .toList();
    }

    private float computeLargeurCase(int maxX, int maxY) {
        return (0.9f * 2) / Math.max(maxX, maxY);
    }


    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE,
                GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(1024, 768, "Submarine 13e jour !", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            } else {
                FoldingInstruction nextFoldingInstruction = event.getNextFoldingInstruction();
                if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE && !animate && nextFoldingInstruction != null) {
                    startNextFoldingAnimation(nextFoldingInstruction);
                }
            }
        });

        GLFWImage image = GLFWImage.malloc();
        GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
        image.set(resource_01.get_width(), resource_01.get_heigh(), resource_01.get_image());
        imagebf.put(0, image);
        glfwSetWindowIcon(window, imagebf);

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2);
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                glViewport(0, 0, width, height);
            }
        });

        // Make the window visible
        glfwShowWindow(window);
    }

    private void startNextFoldingAnimation(FoldingInstruction nextFoldingInstruction) {
        animations = cells.stream()
                .filter(dot -> dot.getDot().shouldMoveDuringFolding(nextFoldingInstruction))
                .map(dot -> new CellAnimation(dot, new CellDisplay(
                                dot.getDot().getFuturePositionAfterFolding(nextFoldingInstruction),
                                largeurCase)
                        )
                )
                .toList();
        animate = true;
    }


    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.2f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            if (animate) {
                if (animationStart == null) {
                    animationStart = System.currentTimeMillis();
                }
                float timeElapsedProportion = (System.currentTimeMillis() - animationStart) / (float) FOLD_DURATION;
                animations.forEach(anim -> anim.getCurrentCellDisplay(timeElapsedProportion).draw());
                if (!resizeAnimation) {
                    // Si on est pendant un folding : on dessine aussi les points qui bougent pas
                    cells.stream()
                            .filter(cell -> !cell.getDot().shouldMoveDuringFolding(event.getNextFoldingInstruction()))
                            .forEach(CellDisplay::draw);
                }
            } else {
                cells.forEach(CellDisplay::draw);
            }

            if (animate && System.currentTimeMillis() - animationStart > FOLD_DURATION) {
                int previousMaxX = currentSituation.maxX();
                int previousMaxY = currentSituation.maxY();
                if (!resizeAnimation) {
                    event.foldOnce();
                }
                initSituation();
                if (Math.max(currentSituation.maxX(), currentSituation.maxY()) != Math.max(previousMaxX, previousMaxY)) {
                    // resize
                    resizeAnimation = true;
                    animations = cells.stream()
                            .map(dot -> new CellAnimation(
                                    new CellDisplay(dot.getDot(), computeLargeurCase(previousMaxX, previousMaxY)),
                                    dot
                            )).toList();
                } else {
                    animate = false;
                    animations = null;
                    resizeAnimation = false;
                    FoldingInstruction nextInstruction = event.getNextFoldingInstruction();
                    if ( nextInstruction != null ) {
                        startNextFoldingAnimation(nextInstruction);
                    }
                }

                animationStart = null;
            }

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

}