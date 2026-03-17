import { marked } from 'marked'
import hljs from 'highlight.js'

export const renderMarkdown = (content: string): string => {
  if (!content) return ''

  const renderer = new marked.Renderer()

  renderer.code = (code: string, language?: string) => {
    const validLanguage = language && hljs.getLanguage(language) ? language : 'plaintext'
    const highlighted = hljs.highlight(code, { language: validLanguage }).value
    return `<pre><code class="hljs language-${validLanguage}">${highlighted}</code></pre>`
  }

  marked.setOptions({
    renderer,
    gfm: true,
    breaks: true
  })

  return marked(content) as string
}
