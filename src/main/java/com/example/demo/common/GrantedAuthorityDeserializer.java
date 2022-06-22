package com.example.demo.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class GrantedAuthorityDeserializer extends JsonDeserializer {

  @Override
  public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    ObjectCodec codec = jsonParser.getCodec();
    JsonNode jsonNode = codec.readTree(jsonParser);
    Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();

    Iterator<JsonNode> elements = jsonNode.elements();
    while (elements.hasNext()) {
      JsonNode next = elements.next();
      JsonNode authority = next.get("authority");
      grantedAuthorities.add(new SimpleGrantedAuthority(authority.asText()));
    }
    return grantedAuthorities;
  }

}
