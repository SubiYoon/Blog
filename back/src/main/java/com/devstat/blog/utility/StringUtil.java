package com.devstat.blog.utility;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StringUtil {

    /**
     * Map<String, List<String>>를 JSON 문자열로 변환
     * 
     * @param folderTree 컨버팅할 Map<String, List<String>> 객체
     * @return JSON 문자열
     * @throws Exception
     */
    public static String convertToJsonTree(Map<String, List<String>> folderTree) throws Exception {
        List<Object> rootList = new ArrayList<>();

        for (String project : folderTree.keySet()) {
            Map<String, Object> rootNode = new LinkedHashMap<>();
            rootNode.put("title", project);
            rootNode.put("type", "folder");
            rootNode.put("filePath", "/" + project);
            rootNode.put("children", new ArrayList<>());

            List<String> paths = folderTree.get(project);
            for (String path : paths) {
                String[] parts = path.split("/");
                List<String> filtered = Arrays.stream(parts)
                        .filter(p -> !p.isEmpty())
                        .toList();

                // 🔽 누적 경로 시작
                insertPath((List<Object>) rootNode.get("children"), filtered, "/" + project);
            }

            rootList.add(rootNode);
        }

        // JSON 문자열로 반환
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootList);
    }

    @SuppressWarnings("unchecked")
    private static void insertPath(List<Object> currentChildren, List<String> parts, String accumulatedPath) {
        if (parts.isEmpty())
            return;

        String name = parts.get(0);
        String filePath = accumulatedPath + "/" + name;

        // 확장자 판별
        String typePart = name.contains(".") ? name.substring(name.lastIndexOf('.') + 1) : "";
        boolean isFile = switch (typePart) {
            case "md", "json", "ts", "js" -> true;
            default -> false;
        };

        String type = isFile ? typePart : "folder";

        Map<String, Object> existing = null;

        for (Object childObj : currentChildren) {
            Map<String, Object> child = (Map<String, Object>) childObj;
            if (child.get("title").equals(name)) {
                existing = child;
                break;
            }
        }

        if (existing == null) {
            existing = new LinkedHashMap<>();
            existing.put("title", name);
            existing.put("type", type);
            existing.put("filePath", filePath);
            if (!isFile) {
                existing.put("children", new ArrayList<>());
            }
            currentChildren.add(existing);
        }

        if (!isFile && parts.size() > 1) {
            insertPath((List<Object>) existing.get("children"), parts.subList(1, parts.size()), filePath);
        }
    }
}
