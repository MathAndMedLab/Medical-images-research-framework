package com.mirf.service;

import com.mirf.queue.ProcessRequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class SegmentationRequestController {

    private QueueManager queueManager;

    @Autowired
    public SegmentationRequestController(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @PostMapping("/lesion")
    public String getSegmentationResult(@RequestParam("mirf-ext") String ext, @RequestParam("file") MultipartFile file){
        return queueManager.getLesionQueue().enqueue(file, ext).toString();
    }

    @GetMapping("/skull-strip")
    public ResponseEntity<Resource> getSkullStripped(@RequestParam("requestId") UUID requestId){

        Resource resource=  queueManager.getBrainExtractorQueue().waitForResult(requestId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("status")
    public String getStatus(@RequestParam("requestId") UUID requestId){
        return queueManager.getStatus(requestId).toString();
    }

    @GetMapping("/lesion")
    public ResponseEntity<Resource> getLesion(@RequestParam("requestId") UUID requestId){

        Resource resource=  queueManager.getLesionQueue().findResult(requestId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/skull-strip")
    public String postSkullStripRequest(@RequestParam("mirf-ext") String ext, @RequestParam("file") MultipartFile file){
        return queueManager.getBrainExtractorQueue().enqueue(file, ext).toString();
    }
}
