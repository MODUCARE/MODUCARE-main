package world.moducare.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import world.moducare.domain.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
