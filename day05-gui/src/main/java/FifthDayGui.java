import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class FifthDayGui {

    public static final int DELAY_NEXTSTEP = 75;
    private static final boolean AUTO_STEP = true;
    // The window handle
    private long window;
    private Day5 event;
    private Long lastTick;

    private final ImageParser resource_01 = ImageParser.load_image("img_2.png");



    public static void main(String[] args) throws IOException {
        new FifthDayGui().run();
    }

    public void run() throws IOException {
        System.out.println("Submarine 5e jour !");


        event = new Day5();
        event.openFile("fifth_day_1.txt");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        event.closeFile();
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
        window = glfwCreateWindow(1024, 768, "Submarine 5e jour !", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            } else if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
                if (event.hasMoreLines()) {
                    eventStep();
                } else {
                    System.out.println("Fichier terminé !");
                }
            }
        });

        GLFWImage image = GLFWImage.malloc(); GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
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

    private void eventStep() {
        try {
            event.readLine();
            lastTick = System.currentTimeMillis();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            if (AUTO_STEP
                    && lastTick != null
                    && System.currentTimeMillis() - lastTick >= DELAY_NEXTSTEP
                    && event.hasMoreLines()) {
                eventStep();
            }
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

//            glColor4f(0.05f, 0.03f, 0.5f, 0.0f);
//            glBegin(GL_QUADS);
//            {
//                glVertex2f(-0.9f, 0.9f);
//                glVertex2f(0.9f, 0.9f);
//                glVertex2f(0.9f, -0.9f);
//                glVertex2f(-0.9f, -0.9f);
//            }
//            glEnd();


            int max = event.getCoveredPoints().keySet()
                    .stream()
                    .flatMap(point -> Stream.of(point.x, point.y))
                    .mapToInt(i -> i + 1)
                    .max().orElse(5);
            float largeurCarre = (0.9f * 2) / max;
//            glColor4f(0.49f, 0.34f, 0.24f, 0.95f);
//            glColor4f(1.0f, 0.99f, 0.01f, 0.95f);
            for (Map.Entry<Day5.Point, AtomicInteger> entry : event.getCoveredPoints().entrySet()) {
//                glColor4f(0.3f * entry.getValue().intValue(), 0.03f, 0.03f, 0.95f);
                glColor4f(0.49f +
                                0.1f * (entry.getValue().intValue()-1)
                        , 0.34f +
                                0.1f * (entry.getValue().intValue()-1)
                        , 0.24f
                                - 0.025f * (entry.getValue().intValue()-1)
                        , 0.95f);
                Day5.Point point = entry.getKey();
                glBegin(GL_QUADS);
                {
                    float originX = (point.x * largeurCarre) - 0.9f;
                    float originY = ((point.y * largeurCarre) - 0.9f) * -1;
                    glVertex2f(originX, originY);
                    glVertex2f(originX + largeurCarre, originY);
                    glVertex2f(originX + largeurCarre, originY - largeurCarre);
                    glVertex2f(originX, originY - largeurCarre);
                }
                glEnd();
            }

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

}