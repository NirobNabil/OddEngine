package imguiLayer;

import imgui.ImGui;
import odd.ParticleCollisionScene;
import odd.Window;

import java.util.HashMap;

public class Debug extends imguiWindow {
    private static HashMap<String, String> messages = new HashMap<String, String>();
    private float[] input = {0f, 0f, 0f, 0f};
    private int counter = 0;

    public static void print(String key, String b) {
        messages.put(key, b);
//        System.out.println(messages.keySet().size());
    }


    public static void remove(String key) {
        messages.remove(key);
    }

    public void update() {
//        System.out.println("updated");
        ImGui.newFrame();
        ImGui.begin("Debug Text");
        ImGui.setWindowSize("Debug Text", 400, 500 );

        for( String key : messages.keySet() ) {
            ImGui.text(key + ": " + messages.get(key));
        }

        ImGui.separator();
        ImGui.text("Input x, y, radius and mass:");
        ImGui.inputFloat4("##", input);
//        System.out.println(input[0] + " " + input[1]);
        boolean ifClicked = ImGui.button("Create Object");

        if(ifClicked == true) {
            counter++;
            if( Window.get().currentScene instanceof ParticleCollisionScene )
                Window.createNewObject("objectx", input[0], input[1], input[2], input[3], false, true);
            else
                Window.createNewObject("objectx", input[0], input[1], input[2], input[3], true, false);

        }

        boolean change_scene = ImGui.button("Change Scene");
        if( change_scene ) {
            if( Window.get().currentScene instanceof ParticleCollisionScene )
                Window.get().changeScene( 1 );
            else Window.get().changeScene( 0 );
        }

        ImGui.end();
    }
}