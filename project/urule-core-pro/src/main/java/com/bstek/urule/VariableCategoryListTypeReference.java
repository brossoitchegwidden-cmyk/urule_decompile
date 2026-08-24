package com.bstek.urule;

import com.bstek.urule.model.library.variable.VariableCategory;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;

/** Type token used when deserializing a variable-category list. */
final class VariableCategoryListTypeReference extends TypeReference<List<VariableCategory>> {
}
