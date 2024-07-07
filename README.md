# SimpleEventBus - Very simple event bus for Java.

## How to use in your Gradle project:

### Add the jitpack Maven repository
```gradle
repositories {
    // Other repos go here, eg. mavenCentral()
    maven { url 'https://jitpack.io' }
}
```

### Add the dependency
```gradle
dependencies {
    implementation 'com.github.AverseMoon:SimpleEventBus:VERSION' // replace VERSION with the version, eg. "v1.0"
}
```

## Some example code to get you started:
```java
import net.simpleeventbus.EventBus;
import net.simpleeventbus.event.Event;
import net.simpleeventbus.Subscribe;

public class Main {
  public static void Main(String[] args) {
    EventBus mybus = EventBus.createDefault(); // Create a basic event bus
    mybus.registerEventType(MyEvent.class); // Register the custom event

    mybus.register(Main.class); // Register any (static) @Subscribe annotations in this class, you can also replace `Main.class` with `this` in a non static context

    mybus.post(new MyEvent("Hello World!"));
  }

  // Simple event listener that listens for all derived events, also remove static if it is in a non static context, you can leave static if you want though.
  @Subscribe
  public static void onEvent(Event evt) {
    System.out.println("Event recieved!");
  }

  // Simple event listener that listens for only our custom event class (MyEvent)
  @Subscribe
  public static void onMyEvent(MyEvent evt) {
    System.out.println("MyEvent recieved!");
    System.out.println(evt.param);
  }
}

public class MyEvent extends Event {
  public String param;

  public MyEvent(String param) {
    this.param = param;
  }
}
```
