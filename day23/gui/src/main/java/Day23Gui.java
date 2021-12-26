import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Day23Gui {

    public static final int LARGEUR = 13;
    private static final long MOVE_DURATION = 1500;
    private Day23 event;
    private long window;
    private Situation initialSituation;
    private Situation currentSituation;
    private Movement currentMovement;
    private LinkedList<Situation> situations = new LinkedList<>();
    private LinkedList<Movement> movements = new LinkedList<>();
    private float largeurCase = 0f;
    private List<CellDisplay> cells;
    private List<CellDisplay> walls;
    private List<CellAnimation> animations = null;
    private boolean animate = false;

    private final ImageParser resource_01 = ImageParser.load_image("img_2.png");
    private Long animationStart = null;
    private Step step;


    public static void main(String[] args) throws IOException {
        new Day23Gui().run();
    }

    public void run() throws IOException {
        System.out.println("Submarine 23e jour !");


        step = Step.STEP_1;
        event = new Day23("input.txt", step);
        System.out.println("Processing...");

        largeurCase = computeLargeurCase(LARGEUR, 3 + step.getChamberCapacity());

        new Thread(() -> {
            initialSituation = event.getBestPath();
            currentSituation = initialSituation;
            movements.add(initialSituation.getMovement());
            situations.add(initialSituation);
            while (currentSituation.getParent() != null) {
                currentSituation = currentSituation.getParent();
                if (currentSituation.getMovement() != null) {
                    movements.add(currentSituation.getMovement());
                }
                situations.add(currentSituation);
            }
            initSituation(step);
        }).start();

        initWalls(step);

        init();

        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void initWalls(Step step) {
        walls = new ArrayList<>();
        for (int i = 0 ; i < 13 ; i++ ) {
            walls.add(new CellDisplay(i, 0, largeurCase));
            if ( i != 3 && i != 5 && i != 7 && i != 9 ) {
                for (int y = 2 ; y < 2 + step.getChamberCapacity() ; y++ ) {
                    walls.add(new CellDisplay(i, y, largeurCase));
                }
            }
            walls.add(new CellDisplay(i, 2 + step.getChamberCapacity(), largeurCase));
        }
        walls.add(new CellDisplay(0, 1, largeurCase));
        walls.add(new CellDisplay(12, 1, largeurCase));
    }

    private void initSituation(Step step) {
        currentSituation = situations.pollLast();
        currentMovement = movements.pollLast();
        cells = Stream.concat(
                        currentSituation.lstChambers().stream()
                                .flatMap(chamber -> IntStream.range(0, chamber.size())
                                        .mapToObj(i -> new CellDisplay(chamber.get(i), chamber, i, largeurCase)))
                        ,
                        IntStream.range(0, currentSituation.parkings().size())
                                .filter(i -> currentSituation.parkings().get(i) != null)
                                .mapToObj(i -> new CellDisplay(currentSituation.parkings().get(i), currentSituation.parkings(), i, largeurCase))
                )
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
        window = glfwCreateWindow(1024, 1024, "Submarine 23e jour !", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            } else {
                if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE && !animate && currentMovement != null) {
                    startNextMovement(currentMovement);
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

    private void startNextMovement(Movement nextMovement) {
        animations = cells.stream()
                .filter(pod -> pod.shouldMove(nextMovement))
                .map(pod -> new CellAnimation(pod, CellDisplay.fromMovement(
                                pod, nextMovement, currentSituation,
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

            if ( walls != null ) {
                walls.forEach(CellDisplay::draw);
            }
            if (cells != null) {
                if (animate) {
                    if (animationStart == null) {
                        animationStart = System.currentTimeMillis();
                    }
                    float timeElapsedProportion = (System.currentTimeMillis() - animationStart) / (float) MOVE_DURATION;
                    animations.forEach(anim -> anim.getCurrentCellDisplay(timeElapsedProportion).draw());
                    // Si on est pendant un move : on dessine aussi les points qui bougent pas
                    cells.stream()
                            .filter(cell -> !cell.shouldMove(currentMovement))
                            .forEach(CellDisplay::draw);

                } else {
                    cells.forEach(CellDisplay::draw);
                }

                if (animate && System.currentTimeMillis() - animationStart > MOVE_DURATION) {  // animation terminée
                    initSituation(step);
                    if (currentMovement != null) {
                        startNextMovement(currentMovement);
                    } else {
                        animate = false;
                    }

                    animationStart = null;
                }
            }

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }


}
