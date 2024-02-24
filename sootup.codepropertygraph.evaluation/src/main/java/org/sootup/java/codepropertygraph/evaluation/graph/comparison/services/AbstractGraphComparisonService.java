package org.sootup.java.codepropertygraph.evaluation.graph.comparison.services;

import io.shiftleft.codepropertygraph.generated.nodes.Method;
import io.shiftleft.semanticcpg.dotgenerator.DotSerializer.Graph;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.sootup.java.codepropertygraph.evaluation.graph.comparison.PropertyGraphComparer;
import org.sootup.java.codepropertygraph.evaluation.graph.processing.JoernProcessor;
import org.sootup.java.codepropertygraph.evaluation.graph.processing.SootUpProcessor;
import org.sootup.java.codepropertygraph.evaluation.graph.util.FileUtils;
import org.sootup.java.codepropertygraph.evaluation.output.ResultWriter;
import sootup.core.model.SootMethod;
import sootup.java.codepropertygraph.propertygraph.PropertyGraph;

public abstract class AbstractGraphComparisonService {
  private final ResultWriter resultWriter = new ResultWriter();

  public void processFilePair(Path cpgPath, Path targetDir, Path resultDirPath) {
    System.out.println("Processing: " + targetDir);
    Map<String, Object> result = new HashMap<>();
    long startTime = System.currentTimeMillis();

    try {
      SootUpProcessor sootUpProcessor = new SootUpProcessor(targetDir);
      JoernProcessor joernProcessor = new JoernProcessor(cpgPath.toString());
      PropertyGraphComparer propertyGraphComparer = new PropertyGraphComparer();

      for (SootMethod sootUpMethod : sootUpProcessor.getMethods()) {
        try {
          if (sootUpMethod.isAbstract() || sootUpMethod.isNative())
            continue; // Todo: handle abstract and native methods

          String methodSignatureAsJoern = sootUpProcessor.getMethodSignatureAsJoern(sootUpMethod);
          Optional<Method> joernMethodOpt = joernProcessor.getMethod(methodSignatureAsJoern);

          if (!joernMethodOpt.isPresent()) continue;

          Method joernMethod = joernMethodOpt.get();

          Graph joernGraph = generateJoernGraph(joernProcessor, joernMethod);
          PropertyGraph sootUpGraph = generateSootUpGraph(sootUpMethod);

          propertyGraphComparer.compare(joernGraph, sootUpGraph, methodSignatureAsJoern);
        } catch (RuntimeException e) {
          e.printStackTrace();
        }
      }

      int similarEdgesCount = propertyGraphComparer.getTotalSameEdges();
      int totalEdges =
          propertyGraphComparer.getTotalSameEdges() + propertyGraphComparer.getTotalDiffEdges();
      double similarityPercentage = ((double) similarEdgesCount / totalEdges) * 100;
      similarityPercentage = Math.round(similarityPercentage * 10000) / 10000.0;

      result.put("numOfMethods", propertyGraphComparer.getTotalMethods());
      result.put("differentEdges", propertyGraphComparer.getTotalDiffEdges());
      result.put("sameEdges", propertyGraphComparer.getTotalSameEdges());
      result.put("similarityPercentage", similarityPercentage + " %");

      result.put("failed", false);

    } catch (Exception e) {
      result.put("failed", true);
    } finally {
      long endTime = System.currentTimeMillis();
      long elapsedTime = endTime - startTime;

      // Break down the elapsed time
      long hours = elapsedTime / (60 * 60 * 1000);
      elapsedTime %= (60 * 60 * 1000);
      long minutes = elapsedTime / (60 * 1000);
      elapsedTime %= (60 * 1000);
      long seconds = elapsedTime / 1000;
      long milliseconds = elapsedTime % 1000;

      Map<String, Long> elapsedTimeDetails = new HashMap<>();
      elapsedTimeDetails.put("hours", hours);
      elapsedTimeDetails.put("minutes", minutes);
      elapsedTimeDetails.put("seconds", seconds);
      elapsedTimeDetails.put("milliseconds", milliseconds);

      result.put("elapsedTime", elapsedTimeDetails);
      result.put("failed", false);

      String baseName = FileUtils.extractBaseName(cpgPath);
      Path resultFilePath = Paths.get(String.valueOf(resultDirPath), baseName + ".json");
      resultWriter.writeResultToFile(resultFilePath, result);
    }
  }

  abstract PropertyGraph generateSootUpGraph(SootMethod sootUpMethod);

  abstract Graph generateJoernGraph(JoernProcessor joernProcessor, Method joernMethod);
}
