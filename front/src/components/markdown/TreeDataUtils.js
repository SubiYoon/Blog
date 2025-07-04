export const updateNodeContent = (tree, path, content) => {
    return tree.map((node) => {
        if (node.filePath === path) {
            return { ...node, content };
        } else if (node.children) {
            return {
                ...node,
                children: updateNodeContent(node.children, path, content),
            };
        }
        return node;
    });
};

export const findNodeByPath = (tree, path) => {
    for (const node of tree) {
        if (node.filePath === path) return node;
        if (node.children) {
            const result = findNodeByPath(node.children, path);
            if (result) return result;
        }
    }
    return null;
};

export const insertChildNode = (tree, parentPath, newNode) => {
    return tree.map((node) => {
        if (node.filePath === parentPath) {
            const children = node.children || [];
            return {
                ...node,
                children: [...children, newNode],
            };
        } else if (node.children) {
            return {
                ...node,
                children: insertChildNode(node.children, parentPath, newNode),
            };
        }
        return node;
    });
};
