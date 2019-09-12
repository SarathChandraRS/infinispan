package org.infinispan.factories.components;

import javax.management.MBeanParameterInfo;

/**
 * JmxOperationParameter stores metadata information about MBean operation parameters which is then used at runtime to
 * build the relevant {@link MBeanParameterInfo}
 *
 * @author Tristan Tarrant
 * @since 5.2.3
 */
public final class JmxOperationParameter {

   private final String name;
   private final String type;
   private final String description;

   public JmxOperationParameter(String name, String type, String description) {
      this.name = name;
      this.type = type;
      this.description = description;
   }

   public String getName() {
      return name;
   }

   public String getType() {
      return type;
   }

   public String getDescription() {
      return description;
   }

   @Override
   public String toString() {
      return "JmxOperationParameter[name=" + name + ", type=" + type + ", description=" + description + "]";
   }
}
