// remark-obsidian-image.js
export function remarkObsidianImage() {
    return (tree) => {
        const visit = require('unist-util-visit');

        visit.visit(tree, 'text', (node, index, parent) => {
            const pattern = /!\[\[(.+?)\]\]/g;
            let match;
            const newNodes = [];

            let lastIndex = 0;
            const value = node.value;

            while ((match = pattern.exec(value)) !== null) {
                const [fullMatch, imagePath] = match;
                const start = match.index;

                if (start > lastIndex) {
                    newNodes.push({
                        type: 'text',
                        value: value.slice(lastIndex, start),
                    });
                }

                newNodes.push({
                    type: 'image',
                    url: `/Attached%20File/${imagePath.replaceAll(" ", "%20")}`,
                    title: null,
                    alt: '',
                });

                lastIndex = start + fullMatch.length;
            }

            if (lastIndex < value.length) {
                newNodes.push({
                    type: 'text',
                    value: value.slice(lastIndex),
                });
            }

            if (newNodes.length > 0) {
                parent.children.splice(index, 1, ...newNodes);
            }
        });
    };
}

