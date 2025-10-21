package com.cop.common.util;

public class Trie {
    private TrieNode root = new TrieNode();

    public void insert(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isEndOfPrefix = true;
    }

    // "Return the longest matching prefix (or null if no match is found)"
    public String longestPrefixMatch(String uri) {
        TrieNode node = root;
        int lastMatchPos = -1;

        for (int i = 0; i < uri.length(); i++) {
            char c = uri.charAt(i);
            if (!node.children.containsKey(c)) {
                break;
            }
            node = node.children.get(c);
            if (node.isEndOfPrefix) {
                lastMatchPos = i;
            }
        }

        if (lastMatchPos == -1) {
            return null;
        }
        return uri.substring(0, lastMatchPos + 1);
    }
}
