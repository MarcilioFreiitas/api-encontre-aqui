package com.encontreaqui.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  @Autowired AdminService adminService;

  @GetMapping("/dashboard-stats")
  public DashboardStatsDTO stats() {
    return adminService.getDashboardStats();
  }

  // Comerciantes pendentes
  @GetMapping("/merchants/pending")
  public List<MerchantDTO> pendingMerchants() {
    return adminService.findPendingMerchants();
  }
  @PostMapping("/merchants/{id}/approve")
  public void approveMerchant(@PathVariable Long id) {
    adminService.approveMerchant(id);
  }
  @PostMapping("/merchants/{id}/reject")
  public void rejectMerchant(@PathVariable Long id) {
    adminService.rejectMerchant(id);
  }

  // Usuários
  @GetMapping("/users")
  public List<UserDTO> allUsers() {
    return adminService.findAllUsers();
  }
  @PostMapping("/users/{id}/suspend")      public void suspendUser(@PathVariable Long id) { adminService.suspendUser(id); }
  @PostMapping("/users/{id}/activate")     public void activateUser(@PathVariable Long id) { adminService.activateUser(id); }
  @DeleteMapping("/users/{id}")            public void deleteUser(@PathVariable Long id) { adminService.deleteUser(id); }

  // Anúncios sinalizados
  @GetMapping("/ads/flagged")
  public List<AdDTO> flaggedAds() {
    return adminService.findFlaggedAds();
  }
  @DeleteMapping("/ads/{id}")        public void removeAd(@PathVariable Long id)  { adminService.deleteAd(id); }
  @PostMapping("/ads/{id}/restore")  public void restoreAd(@PathVariable Long id) { adminService.restoreAd(id); }

  // Comentários sinalizados
  @GetMapping("/comments/flagged")
  public List<CommentDTO> flaggedComments() {
    return adminService.findFlaggedComments();
  }
  @DeleteMapping("/comments/{id}")       public void removeComment(@PathVariable Long id)  { adminService.deleteComment(id); }
  @PostMapping("/comments/{id}/restore") public void restoreComment(@PathVariable Long id) { adminService.restoreComment(id); }
}
