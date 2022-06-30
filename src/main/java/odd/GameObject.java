package odd;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import javax.sound.midi.SysexMessage;
import java.util.ArrayList;
import java.util.List;

public class GameObject {

    public String name;
    private List<Component> components;
    public Transform transform;
    public Scene levelScene;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
    }

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
    }

    public GameObject(String name, Transform transform, Scene levelScene) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.levelScene = levelScene;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component.";
                }
            }
        }

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i=0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {
        this.components.add(c);
        c.gameObject = this;
    }

    public void update(float dt) {
        this.handleDragging();
        for (int i=0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    public void start() {
        for (int i=0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    private int gg = 0;
    private Boolean isBeingDragged = false;
    private Vector2f dragInitialMouseRelativePosition = new Vector2f(0,0);
    private void handleDragging(){
        if( !MouseListener.get().isDragging() ) return;

        MouseListener mouse = MouseListener.get();

        if( mouse.draggingObject == null
            && mouse.getX() >= this.transform.position.x && mouse.getX() <= this.transform.position.x + this.transform.scale.x
            && mouse.getY() >= this.transform.position.y && mouse.getY() <= this.transform.position.y + this.transform.scale.y) {

            mouse.draggingObject = this;
            this.dragInitialMouseRelativePosition = new Vector2f(
                    mouse.getX() - this.transform.position.x,
                    mouse.getY() - this.transform.position.y
            );
        }
        else if( MouseListener.get().draggingObject != this ) return;

        this.transform.position.x = mouse.getX() - this.dragInitialMouseRelativePosition.x;
        this.transform.position.y = mouse.getX() - this.dragInitialMouseRelativePosition.y;

    }
}
