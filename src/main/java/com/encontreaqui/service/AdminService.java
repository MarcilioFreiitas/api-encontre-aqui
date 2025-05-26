// src/main/java/com/encontreaqui/service/AdminService.java
@Service
@Transactional
public class AdminService {

  @Autowired private ComercioRepository    cr;
  @Autowired private ServicoRepository     sr;
  @Autowired private AluguelRepository     ar;
  @Autowired private AvaliacaoRepository   iar;
  @Autowired private UsuarioRepository     ur;

  public DashboardStatsDTO getDashboardStats() {
    DashboardStatsDTO dto = new DashboardStatsDTO();
    dto.setPendingMerchants(cr.findByApprovedFalse().size());
    dto.setActiveUsers(ur.findByActiveTrue().size());
    long countAds = cr.findByFlaggedTrue().size()
                   + sr.findByFlaggedTrue().size()
                   + ar.findByFlaggedTrue().size();
    dto.setFlaggedAds(countAds);
    dto.setFlaggedComments(iar.findByFlaggedTrue().size());
    return dto;
  }

  // merchants
  public List<MerchantDTO>   findPendingMerchants() { return cr.findByApprovedFalse()
                                                            .stream()
                                                            .map(AdminMappers.INSTANCE::toMerchantDTO)
                                                            .collect(Collectors.toList()); }
  public void approveMerchant(Long id) { Comercio c = cr.findById(id).orElseThrow(); c.setApproved(true); cr.save(c); }
  public void rejectMerchant(Long id)  { cr.deleteById(id); }

  // users
  public List<UserDTO> findAllUsers() { return ur.findAll()
                                             .stream()
                                             .map(AdminMappers.INSTANCE::toUserDTO)
                                             .collect(Collectors.toList()); }
  public void suspendUser(Long id)  { Usuario u = ur.findById(id).orElseThrow(); u.setActive(false); ur.save(u); }
  public void activateUser(Long id) { Usuario u = ur.findById(id).orElseThrow(); u.setActive(true);  ur.save(u); }
  public void deleteUser(Long id)   { ur.deleteById(id); }

  // ads
  public List<AdDTO> findFlaggedAds() {
    List<AdDTO> ads = new ArrayList<>();
    cr.findByFlaggedTrue().forEach(c -> ads.add(AdminMappers.INSTANCE.toAdDTOFromComercio(c)));
    sr.findByFlaggedTrue().forEach(s -> ads.add(AdminMappers.INSTANCE.toAdDTOFromServico(s)));
    ar.findByFlaggedTrue().forEach(a -> ads.add(AdminMappers.INSTANCE.toAdDTOFromAluguel(a)));
    return ads;
  }
  public void deleteAd(Long id)   { sr.deleteById(id); ar.deleteById(id); cr.deleteById(id); }
  public void restoreAd(Long id)  {
    cr.findById(id).ifPresent(c -> { c.setFlagged(false); c.setFlagReason(null); cr.save(c); });
    sr.findById(id).ifPresent(s -> { s.setFlagged(false); s.setFlagReason(null); sr.save(s); });
    ar.findById(id).ifPresent(a -> { a.setFlagged(false); a.setFlagReason(null); ar.save(a); });
  }

  // comments
  public List<CommentDTO> findFlaggedComments() {
    return iar.findByFlaggedTrue()
              .stream()
              .map(AdminMappers.INSTANCE::toCommentDTO)
              .collect(Collectors.toList());
  }
  public void deleteComment(Long id)  { iar.deleteById(id); }
  public void restoreComment(Long id) {
    Avaliacao a = iar.findById(id).orElseThrow();
    a.setFlagged(false);
    a.setFlagReason(null);
    iar.save(a);
  }
}
