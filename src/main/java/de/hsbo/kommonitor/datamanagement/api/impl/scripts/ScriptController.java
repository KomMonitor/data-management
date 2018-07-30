package de.hsbo.kommonitor.datamanagement.api.impl.scripts;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsbo.kommonitor.datamanagement.api.ProcessScriptsApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessScriptOverviewType;
import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessScriptPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessScriptPUTInputType;

@Controller
public class ScriptController extends BasePathController implements ProcessScriptsApi {

	private static Logger logger = LoggerFactory.getLogger(ScriptController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	ScriptManager scriptManager;

	@org.springframework.beans.factory.annotation.Autowired
	public ScriptController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	public ResponseEntity addProcessScriptAsBody(ProcessScriptPOSTInputType processScriptData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity deleteProcessScript(String indicatorId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<String> getProcessScriptTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<ProcessScriptOverviewType>> getProcessScripts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<ProcessScriptOverviewType>> getProcessScriptsForIndicator(String indicatorId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity updateProcessScriptAsBody(String indicatorId, ProcessScriptPUTInputType processScriptData) {
		// TODO Auto-generated method stub
		return null;
	}
}
