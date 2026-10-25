import User from "@/components/User";
import { components } from "@/types/api";

type Props = {
  user: components["schemas"]["UserResponse"];
};

export function InvitedBy({ user }: Props) {
  return (
    <div className="flex items-center gap-x-2">
      <p>Invited by:</p>
      <User user={user} />
    </div>
  );
}
