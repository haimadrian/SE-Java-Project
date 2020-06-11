package org.spa.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.spa.controller.user.User;

public abstract class AbstractUser implements User {
   @JsonProperty
   protected String userId;
}
