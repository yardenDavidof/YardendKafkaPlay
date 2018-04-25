import java.time.OffsetDateTime;
import java.util.UUID;

public class TestObject {

    private String name;
    private UUID uuid;
    private int age;
    private OffsetDateTime creationTime;

    public TestObject(String name, int age) {
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.age = age;
        this.creationTime = OffsetDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUUId() {
        return uuid;
    }

    public void setUUId(UUID id) {
        this.uuid = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public OffsetDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(OffsetDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "TestObject{" +
                "name='" + name + '\'' +
                ", uuid=" + uuid +
                ", age=" + age +
                ", creationTime=" + creationTime +
                '}';
    }
}
