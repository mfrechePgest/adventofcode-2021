import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Day11Gui {

    public static final int DELAY_NEXTSTEP = 130;
    public static final int DELAY_DURING_PROPAGATION = 30;
    public static final float FP_STEP = 75f;
    // The window handle
    private long window;
    private Day11 event;
    private Day11.Situation currentSituation = null;
    private Thread computationThread = null;

    private Colors[][] currentColors;
    private Float[][] currentEspacement = null;


    private final ImageParser resource_01 = ImageParser.load_image("img_2.png");


    public static void main(String[] args) throws IOException {
        new Day11Gui().run();
    }

    public void run() throws IOException {
        System.out.println("Submarine 11e jour !");


        event = new Day11();
        event.openFile("day_11_1.txt");
        while (event.hasMoreLines()) {
            event.readLine();
        }
        event.closeFile();
        this.currentSituation = event.getSituation();
        initCurrentColors();

        init();

        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void initCurrentColors() {
        currentColors = new Colors[currentSituation.octopusses().length][currentSituation.octopusses()[0].length];
        for (int x = 0; x < currentSituation.octopusses().length; x++) {
            for (int y = 0; y < currentSituation.octopusses()[x].length; y++) {
                int value = currentSituation.octopusses()[x][y];
                currentColors[x][y] = mapValueToColor(value, currentSituation.enlightened().contains(new Day11.Cell(x, y)));
            }
        }
    }

    private Colors mapValueToColor(int value, boolean enlightened) {
        if (enlightened || value > 9) {
            return new Colors(0.973f, 0.737f, 0.22f, 0.95f);
        }
        return switch (value) {
            case 1 -> new Colors(0.53f, 0.549f, 0.533f, 0.95f);
            case 2 -> new Colors(0.439f, 0.439f, 0.439f, 0.95f);
            case 3 -> new Colors(0.314f, 0.329f, 0.314f, 0.95f);
            case 4 -> new Colors(0.22f, 0.157f, 0.031f, 0.95f);
            case 5 -> new Colors(0.314f, 0.235f, 0.063f, 0.95f);
            case 6 -> new Colors(0.439f, 0.329f, 0.094f, 0.95f);
            case 7 -> new Colors(0.533f, 0.408f, 0.125f, 0.95f);
            case 8 -> new Colors(0.659f, 0.486f, 0.157f, 0.95f);
            case 9 -> new Colors(0.753f, 0.565f, 0.188f, 0.95f);
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
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
        window = glfwCreateWindow(1024, 768, "Submarine 11e jour !", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
                if (computationThread != null) {
                    computationThread.interrupt();
                }
            } else if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE && computationThread == null) {
                computationThread = new Thread(() -> event.compute((situation, duringPropagation) -> {
                    currentSituation = situation;
                    try {
                        Thread.sleep(duringPropagation ? DELAY_DURING_PROPAGATION : DELAY_NEXTSTEP);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }, true));
                computationThread.start();
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

            float largeurCaseOctopus = (0.9f * 2) / currentSituation.octopusses().length;
            final float espacement = largeurCaseOctopus / 7f;

            for (int x = 0; x < currentSituation.octopusses().length; x++) {
                for (int y = 0; y < currentSituation.octopusses()[x].length; y++) {
                    int value = currentSituation.octopusses()[x][y];
                    boolean isEnlightened = currentSituation.enlightened().contains(new Day11.Cell(x, y));
                    Colors targetColor = mapValueToColor(value, isEnlightened);
                    currentColors[x][y] = new Colors(currentColors[x][y].red + ((targetColor.red - currentColors[x][y].red) / FP_STEP)
                            , currentColors[x][y].green + ((targetColor.green - currentColors[x][y].green) / FP_STEP)
                            , currentColors[x][y].blue + ((targetColor.blue - currentColors[x][y].blue) / FP_STEP)
                            , currentColors[x][y].alpha + ((targetColor.alpha - currentColors[x][y].alpha) / FP_STEP));
                    glColor4f(currentColors[x][y].red,
                            currentColors[x][y].green,
                            currentColors[x][y].blue,
                            currentColors[x][y].alpha);
                    float octopusEspacement = espacement;
                    if (value < 9 && !isEnlightened) {
                        octopusEspacement += espacement - (espacement * (value / 9f));
                    }
                    if (currentEspacement == null || currentEspacement[x][y] == null) {
                        currentEspacement = new Float[currentSituation.octopusses().length][currentSituation.octopusses().length];
                        currentEspacement[x][y] = octopusEspacement;
                    } else {
                        currentEspacement[x][y] = currentEspacement[x][y] + ((octopusEspacement - currentEspacement[x][y]) / FP_STEP);
                    }
                    octopusEspacement = currentEspacement[x][y];
                    glBegin(GL_QUADS);
                    {
                        float originX = (x * largeurCaseOctopus) - 0.9f;
                        float originY = ((y * largeurCaseOctopus) - 0.9f) * -1;
                        glVertex2f(originX  + octopusEspacement, originY - octopusEspacement);
                        glVertex2f(originX + largeurCaseOctopus - octopusEspacement, originY - octopusEspacement);
                        glVertex2f(originX + largeurCaseOctopus - octopusEspacement, originY - largeurCaseOctopus + octopusEspacement);
                        glVertex2f(originX  + octopusEspacement, originY - largeurCaseOctopus + octopusEspacement);
                    }
                    glEnd();
                }
            }

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }


    private record Colors(float red, float green, float blue, float alpha) {
    }

}