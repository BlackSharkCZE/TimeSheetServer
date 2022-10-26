package cz.blackshark.modules.main.persistence.entity

import javax.persistence.*

@Entity
@Table(name = "remote_write_settings")
@Cacheable
class RemoteWriteSettingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "remote_write_settings_seq")
    @SequenceGenerator(
        name = "remote_write_settings_seq",
        sequenceName = "remote_write_settings_seq",
        allocationSize = 1
    )
    var id: Long? = null

    @Column(name = "root_project_id", nullable = false)
    var projectRootId: Long? = null

    @Column(name = "project_id", nullable = false)
    var projectId: Long? = null

    @Column(name = "key_pattern", nullable = false)
    var keyPattern: String? = null

    @Column(name = "remote_writer_list", nullable = false)
    var remoteWriterList: String? = null

    @Column(name = "tag_id", nullable = true)
    var tagId: Int? = null

    @Column(name = "root_project_name")
    var rootProjectName: String? = null

    @Column(name = "project_name")
    var projectName: String? = null

}
