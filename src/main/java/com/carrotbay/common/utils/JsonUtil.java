package com.carrotbay.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtil {

	public static void getRelatedEntitiesToId(JsonNode jsonNode) {
		if (jsonNode.isObject()) {
			jsonNode.fieldNames().forEachRemaining(field -> {
				JsonNode value = jsonNode.get(field);

				if (value.isObject()) {
					JsonNode idNode = value.get("id");
					if (idNode != null) {
						((ObjectNode)value).removeAll();
						((ObjectNode)value).set("id", idNode);
					}
					getRelatedEntitiesToId(value);
				}
			});
		}
	}
}
