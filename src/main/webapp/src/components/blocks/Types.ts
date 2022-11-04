export type GemProject = {
    key: number,
    text: string,
    parentId: number | null,
    projects: Array<GemProject> | null,
    tags: Array<GemProject> | null,
}
