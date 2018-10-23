package com.bps.web.view;

import java.util.Map;

import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.bps.web.util.Result;

public class CommonAjaxView extends MappingJacksonJsonView {
	@Override
	protected Object filterModel(Map<String, Object> model) {
		Map<?, ?> result = (Map<?, ?>) super.filterModel(model);
		if (result != null) {
			Object root = result.get(Result.DEFAULT_MODEL_KEY);
			if (root != null) {
				return root;
			}
		}
		return result;
	}

}
