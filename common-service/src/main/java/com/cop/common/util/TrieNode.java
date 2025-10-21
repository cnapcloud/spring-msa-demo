package com.cop.common.util;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfPrefix = false;
}