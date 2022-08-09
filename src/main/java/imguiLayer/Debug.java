package imguiLayer;

import imgui.ImGui;

import java.util.HashMap;

public class Debug extends imguiWindow {
    private static HashMap<String, String> messages = new HashMap<String, String>();

    public static void print(String key, String b) {
        messages.put(key, b);
        System.out.println(messages.keySet().size());
    }


    public static void remove(String key) {
        messages.remove(key);
    }

    public void update() {
        System.out.println("updated");
        ImGui.newFrame();
        ImGui.begin("Debug Text");
        ImGui.setWindowSize("Debug Text", 400, 800 );

        for( String key : messages.keySet() ) {
            ImGui.text(key + ": " + messages.get(key));
        }

        ImGui.end();
    }

}
