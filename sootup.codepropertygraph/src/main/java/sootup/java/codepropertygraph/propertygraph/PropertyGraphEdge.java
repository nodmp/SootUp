package sootup.java.codepropertygraph.propertygraph;

import java.util.Objects;

public class PropertyGraphEdge {
  private final PropertyGraphNode source;
  private final PropertyGraphNode destination;
  private final String label;

  public PropertyGraphEdge(PropertyGraphNode source, PropertyGraphNode destination, String label) {
    this.source = source;
    this.destination = destination;
    this.label = label;
  }

  public PropertyGraphNode getSource() {
    return source;
  }

  public PropertyGraphNode getDestination() {
    return destination;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PropertyGraphEdge that = (PropertyGraphEdge) o;
    return Objects.equals(source, that.source)
        && Objects.equals(destination, that.destination)
        && Objects.equals(label, that.label);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, destination, label);
  }
}
