type EventCallback<T = any> = (payload: T) => void

interface EventBus {
  events: Map<string, Set<EventCallback>>
  on<T = any>(event: string, callback: EventCallback<T>): () => void
  emit<T = any>(event: string, payload?: T): void
  off<T = any>(event: string, callback?: EventCallback<T>): void
}

const eventBus: EventBus = {
  events: new Map<string, Set<EventCallback>>(),

  on<T = any>(event: string, callback: EventCallback<T>) {
    if (!this.events.has(event)) {
      this.events.set(event, new Set())
    }
    this.events.get(event)!.add(callback)
    
    return () => this.off(event, callback)
  },

  emit<T = any>(event: string, payload?: T) {
    const callbacks = this.events.get(event)
    if (callbacks) {
      callbacks.forEach(callback => callback(payload))
    }
  },

  off<T = any>(event: string, callback?: EventCallback<T>) {
    if (!callback) {
      this.events.delete(event)
    } else {
      const callbacks = this.events.get(event)
      if (callbacks) {
        callbacks.delete(callback)
      }
    }
  }
}

export function useEventBus() {
  return eventBus
}

export const GlobalEvents = {
  REFRESH_ARTICLE: 'refresh-article',
  REFRESH_TAG: 'refresh-tag',
  REFRESH_CATEGORY: 'refresh-category',
  THEME_CHANGED: 'theme-changed',
  USER_UPDATED: 'user-updated',
  LOGOUT: 'logout'
} as const
