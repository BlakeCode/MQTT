package mqtt;

import java.util.ArrayList;

/**
 * @author blake
 * date 2020-10-02 14:18:57
 **/

public class MqttProperties {

    private ArrayList<MqttProperties.MqttProperty> propertyList = new ArrayList<>();
    private int propertiesByteLength;

    public ArrayList<MqttProperties.MqttProperty> getPropertyList() { return propertyList; }

    public void add(MqttProperties.MqttProperty mqttProperty) { propertyList.add(mqttProperty); }
    public void remove(MqttProperties.MqttProperty mqttProperty) { propertyList.remove(mqttProperty); }

    public int getPropertiesByteLength() { return this.propertiesByteLength; }
    public void setPropertiesByteLength(int length) { this.propertiesByteLength = length; }

    /**
     * @author blake
     * date 2020-10-07 16:42:41
     **/

    public class MqttProperty<T> {

        private int propertyId;
        private T value;

        public MqttProperty(T value, int propertyId) {
            this.value = value;
            this.propertyId = propertyId;
        }

        public int getPropertyId() { return propertyId; }

        public T getValue() { return value; }
    }
}
